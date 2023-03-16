package com.example.phakezalo.database.viewModels

import androidx.lifecycle.ViewModel
import com.example.phakezalo.database.network.FirebaseInstance
import com.example.phakezalo.models.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageViewModel:ViewModel() {
    fun getMessages(id: String, callback: (ArrayList<Message>) -> Unit) {
        val messages = ArrayList<Message>()

        FirebaseInstance.dbRef().child(id).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val dataMss = snap.getValue(Message::class.java)
                        messages.add(dataMss!!)
                    }
                    messages.reverse()
                    callback(messages)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun sendMessage(isSend:Boolean, text:String,id: String, time:String,type:String){
        FirebaseInstance.dbRef().child(id).child("messages")
            .child(FirebaseInstance.dbRef().push().key!!.toString())
            .setValue(Message(text,time,isSend,type))
            .addOnCompleteListener {
            }
            .addOnCanceledListener {
            }
    }
}