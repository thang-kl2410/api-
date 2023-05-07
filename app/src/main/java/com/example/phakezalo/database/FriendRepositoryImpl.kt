package com.example.phakezalo.database

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.phakezalo.database.network.FirebaseInstance
import com.example.phakezalo.database.repository.FriendRepository
import com.example.phakezalo.models.Friend
import com.example.phakezalo.models.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor():FriendRepository {
    val listFriend: MutableLiveData<List<Friend>> = MutableLiveData()
    val listAllFriend: MutableLiveData<List<Friend>> = MutableLiveData()
    val listDeletedFriends: MutableLiveData<List<Friend>> = MutableLiveData()
    private lateinit var messages:ArrayList<Message>
    private val isLoading = MutableLiveData<Boolean>()

    override fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    override fun getFriends(): LiveData<List<Friend>> {
        isLoading.value = true
        messages = arrayListOf()
        if (listFriend.value == null) listFriend.value = arrayListOf()
        FirebaseInstance.dbRef()
            .orderByChild("endMessageTime")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (friendSnapshot in snapshot.children) {
                    val friend = friendSnapshot.getValue(Friend::class.java)
                    if (friend?.isDeleted == false) {
                        friends.add(friend)
                    }
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

    override fun getDeletedFriends(): LiveData<List<Friend>> {
        isLoading.value = true
        messages = arrayListOf()
        if (listDeletedFriends.value == null) listDeletedFriends.value = arrayListOf()
        FirebaseInstance.dbRef()
            .orderByChild("endMessageTime")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (friendSnapshot in snapshot.children) {
                    val friend = friendSnapshot.getValue(Friend::class.java)
                    if (friend?.isDeleted == true) {
                        friends.add(friend)
                    }
                }
                listDeletedFriends.postValue(friends)
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FriendViewModel", error.message, error.toException())
            }
        })
        return listDeletedFriends
    }

    override fun getAllFriends(): LiveData<List<Friend>> {
        isLoading.value = true
        messages = arrayListOf()
        if (listAllFriend.value == null) listAllFriend.value = arrayListOf()
        FirebaseInstance.dbRef()
            .orderByChild("endMessageTime")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = mutableListOf<Friend>()
                for (friendSnapshot in snapshot.children) {
                    val friend = friendSnapshot.getValue(Friend::class.java)
                    friend?.let { friends.add(it) }
                }
                listAllFriend.postValue(friends)
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FriendViewModel", error.message, error.toException())
            }
        })
        return listAllFriend
    }

    override fun insertFriend(name: String, uri: Uri) {
        try{
            isLoading.value = true
            if (uri == null) {
                return
            }
            val storageRef = FirebaseInstance.storageRef()
            storageRef.putFile(uri)
                .addOnSuccessListener { _ ->
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUri = uri.toString()
                        val id = FirebaseInstance.dbRef().push().key.toString()
                        FirebaseInstance.dbRef().child(id).setValue(Friend(id,name, null,imageUri,false, System.currentTimeMillis()*(-1),System.currentTimeMillis()*(-1)))
                            .addOnSuccessListener {
                                isLoading.value = false
                            }.addOnFailureListener {
                            }
                    }
                }
        } catch (e:Exception){
            Log.i("Error when inserting:", e.toString())
        }
    }

    override fun changeStateDelete(id: String, state: Boolean) {
        val updatesMap = HashMap<String, Any>()
        updatesMap["deleted"] = state
        FirebaseInstance.dbRef().child(id).updateChildren(updatesMap)
            .addOnSuccessListener {
            }.addOnFailureListener {
            }
    }

    override fun deleteFriend(id: String) {
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

    override fun updateDataFriend(id: String, name: String, uri: Uri) {
        val updatesMap = HashMap<String, Any>()
        updatesMap["name"] = name

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

    override fun updateNameFriend(id: String, name: String) {
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