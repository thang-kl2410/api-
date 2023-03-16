package com.example.phakezalo.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.phakezalo.database.network.FirebaseInstance
import com.example.phakezalo.database.repository.MessageRepository
import com.example.phakezalo.models.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class MessageViewModel:ViewModel() {
    private val messageRepository = MessageRepository()
    fun getMessages(id: String, callback: (ArrayList<Message>) -> Unit) {
        messageRepository.getMessages(id){
            callback(it)
        }
    }

    fun sendMessage(isSend:Boolean, text:String,id: String, time:String,type:String) = messageRepository.sendMessage(isSend, text, id, time,type)

    fun removeMessage(idUser:String,id:String) = messageRepository.removeMessage(idUser,id)

    fun getEndMessage(id:String,callback: (Message) -> Unit) {
        try {
            messageRepository.getEndMessage(id){
                callback(it)
            }
        } catch (e:Exception){
            Log.i("Firebase: ", e.toString() )
        }
    }
}