package com.example.phakezalo.database.repository

import com.example.phakezalo.models.Message

interface MessageRepository {
    fun getMessages(id: String, callback: (ArrayList<Message>) -> Unit)
    fun sendMessage(isSend:Boolean, text:String,id: String, time:String,type:String)
    fun removeMessage(idUser:String,id:String)
    fun getEndMessage(id: String, callback: (Message) -> Unit)
}