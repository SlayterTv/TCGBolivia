package com.slaytertv.tcgbolivia.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardDosItem(
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
    var useridcard: String = "",
    val set_name: String="",
    val set_code: String="",
    val set_rarity: String="",
    val set_rarity_code: String="",
    val set_price: String=""
): Parcelable
data class CardDosResponse(
    val data: List<CardDosItem>
)