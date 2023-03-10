package com.example.phakezalo.models

data class Friend(
    var id: String? = "",
    var name: String? = "",
    val messages: Map<String, Message>? = null,
    var avatar: String? = "",
    var isDeleted: Boolean? = false
){
    val messageList: List<Message>
        get() = messages?.values?.toList() ?: emptyList()
}