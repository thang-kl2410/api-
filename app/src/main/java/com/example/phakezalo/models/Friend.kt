package com.example.phakezalo.models

data class Friend(
    var id: String? = "",
    var name: String? = "",
    val messages: Map<String, Message>? = mapOf(),
    var avatar: String? = "",
    var isDeleted: Boolean? = false,
    var beforeEndMessageTime: Long? = 0,
    var endMessageTime: Long? = 0
){
    val messageList: List<Message>
        get() = messages?.values?.toList() ?: emptyList()
}