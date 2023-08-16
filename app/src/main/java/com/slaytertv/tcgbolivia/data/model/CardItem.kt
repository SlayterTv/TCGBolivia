package com.slaytertv.tcgbolivia.data.model

data class CardItem(
    val id: Int,
    val name: String,
    val type: String,
    val frameType: String,
    val desc: String,
    val atk: Int,
    val def: Int,
    val level: Int,
    val race: String,
    val attribute: String,
    val imageUrl: String,
    var precio:Int,
    var cantidad:Int,
    var user:String,
    var useridcard:String
    // Otros campos relevantes para una carta, como ataque, defensa, efectos, etc.
)


data class CardResponse(val data: List<CardItem>)