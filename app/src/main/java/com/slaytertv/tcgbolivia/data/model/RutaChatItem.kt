package com.slaytertv.tcgbolivia.data.model

data class RutaChatItem (
    val id:Int = 0,
    val name:String ="",
    val image:String = "",
    val rutachat:String = ""
)

data class ChatMessageItem(
    val senderId: String,
    val text: String,
    val timestamp: Long,
    val isCurrentUser:String = ""
)
