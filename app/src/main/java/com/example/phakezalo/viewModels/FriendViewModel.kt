package com.example.phakezalo.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phakezalo.database.network.FirebaseInstance
import com.example.phakezalo.database.repository.FriendRepository
import com.example.phakezalo.models.Friend
import com.example.phakezalo.models.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener



class FriendViewModel :ViewModel() {
    var listFriend:MutableLiveData<List<Friend>> = MutableLiveData()
    private val friendRepository = FriendRepository()

    init {
        listFriend = friendRepository.getFriends() as MutableLiveData<List<Friend>>
    }
    fun isLoading(): LiveData<Boolean> {
        return friendRepository.isLoading()
    }
    fun insertFriend(name:String, uri:Uri) = friendRepository.insertFriend(name, uri)

    fun deleteFriend(id:String) = friendRepository.deleteFriend(id)

    fun updateDataFriend(id:String, name:String, uri:Uri) = friendRepository.updateDataFriend(id, name, uri)

    fun updateNameFriend(id:String, name:String) = friendRepository.updateNameFriend(id,name)

}