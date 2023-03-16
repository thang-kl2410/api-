package com.example.phakezalo.database.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.phakezalo.database.network.FirebaseInstance
import com.example.phakezalo.models.Friend
import com.example.phakezalo.models.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FriendRepository {
    val listFriend: MutableLiveData<List<Friend>> = MutableLiveData()
    private lateinit var messages:ArrayList<Message>
    private val isLoading = MutableLiveData<Boolean>()

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getFriends(): LiveData<List<Friend>> {
        isLoading.value = true
        messages = arrayListOf<Message>()
        if (listFriend.value == null) listFriend.value = arrayListOf()
        FirebaseInstance.dbRef().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (friendSnapshot in snapshot.children) {
                    val friend = friendSnapshot.getValue(Friend::class.java)
                    friend?.let { friends.add(it) }
                }
                listFriend.postValue(friends)
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FriendViewModel", error.message, error.toException())
            }
        })
        return listFriend
    }

    fun insertFriend(name:String, uri: Uri){
        isLoading.value = true
        if (uri == null) {
            return
        }
        //tải lên hình ảnh vào Firebase Storage
        val storageRef = FirebaseInstance.storageRef()
        storageRef?.putFile(uri)
            ?.addOnSuccessListener { _ ->
                // lấy đường dẫn URL của hình ảnh mới được tải lên.
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    val imageUri = uri.toString()
                    val id = FirebaseInstance.dbRef().push().key.toString()
                    FirebaseInstance.dbRef().child(id).setValue(Friend(id,name, null,imageUri,false))
                        .addOnSuccessListener {
                            isLoading.value = false
                        }.addOnFailureListener {
                        }
                }
            }
    }

    fun deleteFriend(id:String){
        isLoading.value = true
        FirebaseInstance.dbRef().child(id).removeValue()
            .addOnSuccessListener {
                isLoading.value = false
                Log.d("FriendViewModel", "Xóa bạn bè thành công: $id")
            }
            .addOnFailureListener {
                Log.e("FriendViewModel", "Lỗi khi xóa bạn bè $id", it)
            }
    }

    fun updateDataFriend(id:String, name:String, uri: Uri){
        isLoading.value = true
        val updatesMap = HashMap<String, Any>()
        updatesMap["name"] = name

        if (uri != null) {
            val storageRef = FirebaseInstance.storageRef()
            storageRef.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUri = uri.toString()
                        updatesMap["avatar"] = imageUri

                        FirebaseInstance.dbRef().child(id).updateChildren(updatesMap)
                            .addOnSuccessListener {
                                isLoading.value = false
                            }.addOnFailureListener {
                            }
                    }
                }.addOnFailureListener {
                }
        }
    }

    fun updateNameFriend(id:String, name:String){
        isLoading.value = true
        val updatesMap = HashMap<String, Any>()
        updatesMap["name"] = name
        FirebaseInstance.dbRef().child(id).updateChildren(updatesMap)
            .addOnSuccessListener {
                isLoading.value = false
            }.addOnFailureListener {
            }
    }
}