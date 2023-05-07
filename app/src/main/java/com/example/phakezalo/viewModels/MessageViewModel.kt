package com.example.phakezalo.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.phakezalo.database.FriendRepositoryImpl
import com.example.phakezalo.database.MessageRepositoryImpl
import com.example.phakezalo.database.repository.MessageRepository
import com.example.phakezalo.models.Message
import java.lang.Exception
import javax.inject.Inject

class MessageViewModel @Inject constructor(private val messageRepository:MessageRepositoryImpl) {
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