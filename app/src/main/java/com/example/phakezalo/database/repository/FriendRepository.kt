package com.example.phakezalo.database.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.phakezalo.models.Friend

interface FriendRepository {
    fun isLoading(): LiveData<Boolean>
    fun getFriends(): LiveData<List<Friend>>
    fun getDeletedFriends(): LiveData<List<Friend>>
    fun getAllFriends(): LiveData<List<Friend>>
    fun insertFriend(name:String, uri: Uri)
    fun changeStateDelete(id:String, state:Boolean)
    fun deleteFriend(id:String)
    fun updateDataFriend(id:String, name:String, uri: Uri)
    fun updateNameFriend(id:String, name:String)
}