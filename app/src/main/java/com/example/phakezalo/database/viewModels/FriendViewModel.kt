package com.example.phakezalo.database.viewModels

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phakezalo.models.Friend
import com.example.phakezalo.models.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class FriendViewModel():ViewModel() {
    val listFriend:MutableLiveData<List<Friend>> = MutableLiveData()

    private val dbRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("Friends")
    }

    private var storage = Firebase.storage

    private lateinit var messages:ArrayList<Message>

    init {
        getFriends()
        storage = FirebaseStorage.getInstance()
    }

    private fun getFriends(){
        messages = arrayListOf<Message>()
        if (listFriend.value == null) listFriend.value = arrayListOf()
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (friendSnapshot in snapshot.children) {
                    val friend = friendSnapshot.getValue(Friend::class.java)
                    friend?.let { friends.add(it) }
                }
                listFriend.postValue(friends)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FriendViewModel", error.message, error.toException())
            }
        })
    }

    fun insertFriend(name:String, uri:Uri){
        if (uri == null) {
            return
        }
        //tải lên hình ảnh vào Firebase Storage
        val storageRef = storage?.getReference("Images")?.child("${System.currentTimeMillis()}")
        storageRef?.putFile(uri!!)
            ?.addOnSuccessListener { _ ->
                // lấy đường dẫn URL của hình ảnh mới được tải lên.
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUri = uri.toString()
                    val dbRef = FirebaseDatabase.getInstance().getReference("Friends")
                    val id = dbRef.push().key.toString()
                    dbRef.child(id).setValue(Friend(id,name, null,imageUri,false))
                        .addOnSuccessListener {

                        }.addOnFailureListener {
                        }
                }
            }
    }

    fun deleteFriend(id:String){
        dbRef.child(id).removeValue()
            .addOnSuccessListener {
                Log.d("FriendViewModel", "Xóa bạn bè thành công: $id")
            }
            .addOnFailureListener {
                Log.e("FriendViewModel", "Lỗi khi xóa bạn bè $id", it)
            }
    }

    fun updateDataFriend(id:String, name:String, uri:Uri){
        if (uri == null) {
            return
        }

        val storageRef = storage?.getReference("Images")?.child("${System.currentTimeMillis()}")
        storageRef?.putFile(uri!!)
            ?.addOnSuccessListener { taskSnapshot ->
                // lấy đường dẫn URL của hình ảnh mới được tải lên.
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUri = uri.toString()
                    val updatesMap = HashMap<String, Any>()
                    updatesMap["name"] = name
                    updatesMap["avatar"] = imageUri

                    dbRef.child(id).updateChildren(updatesMap)
                        .addOnSuccessListener {

                        }.addOnFailureListener {

                        }
                }
            }
    }
}