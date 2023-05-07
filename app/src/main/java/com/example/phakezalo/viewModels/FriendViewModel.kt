package com.example.phakezalo.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.phakezalo.database.FriendRepositoryImpl
import com.example.phakezalo.models.Friend
import javax.inject.Inject


class FriendViewModel @Inject constructor(private val friendRepository:FriendRepositoryImpl){
    var listFriend:MutableLiveData<List<Friend>> = MutableLiveData()
    var allFriend:MutableLiveData<List<Friend>> = MutableLiveData()
    var listDeletedFriend:MutableLiveData<List<Friend>> = MutableLiveData()

    init {
        listFriend =friendRepository.getFriends() as MutableLiveData<List<Friend>>
        allFriend = friendRepository.getAllFriends() as MutableLiveData<List<Friend>>
        listDeletedFriend = friendRepository.getDeletedFriends() as MutableLiveData<List<Friend>>
    }
    fun isLoading(): LiveData<Boolean> {
        return friendRepository.isLoading()
    }
    fun insertFriend(name:String, uri:Uri) = try{
        friendRepository.insertFriend(name, uri)
    } catch (e:Exception){
        Log.i("Error when inserting:", e.toString())
    }

    fun deleteFriend(id:String) = friendRepository.deleteFriend(id)

    fun changeStateDelete(id:String, state:Boolean) = friendRepository.changeStateDelete(id, state)

    fun updateDataFriend(id:String, name:String, uri:Uri) = friendRepository.updateDataFriend(id, name, uri)

    fun updateNameFriend(id:String, name:String) = friendRepository.updateNameFriend(id,name)

}