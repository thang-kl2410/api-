package com.example.phakezalo.database.network

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseInstance {
    private val deviceId = android.os.Build.ID!!.toString().replace(".","")
    fun dbRef() = FirebaseDatabase.getInstance().getReference(deviceId).child("Friends")
    fun storageRef():StorageReference{
        val storage = FirebaseStorage.getInstance()
        return storage.getReference("Images").child("${System.currentTimeMillis()}")
    }

}