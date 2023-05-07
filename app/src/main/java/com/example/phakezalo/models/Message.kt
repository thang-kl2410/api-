package com.example.phakezalo.models

data class Message(
    var id:String? = "",
    var message:String? = "",
    var time:String? = "",
    var isSend:Boolean? = false,
    var type:String? = ""
)
