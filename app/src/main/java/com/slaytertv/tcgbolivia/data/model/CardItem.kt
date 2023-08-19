package com.slaytertv.tcgbolivia.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardItem(
    var id: Int = 0,
    var idcard:String = "",
    val name: String = "",
    val type: String = "",
    val frameType: String = "",
    val desc: String = "",
    val atk: Int = 0,
    val def: Int = 0,
    val level: Int = 0,
    val race: String = "",
    val attribute: String = "",
    val imageUrl: String = "",
    var precio: Int = 0,
    var cantidad: Int = 0,
    var user: String = "",
    var useridcard: String = ""
):Parcelable
data class CardResponse(
    val data: List<CardItem>
)

