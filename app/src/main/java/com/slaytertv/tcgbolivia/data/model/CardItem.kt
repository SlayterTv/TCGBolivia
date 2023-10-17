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
    var juego:String ="",
    //var precio: Int = 0,
    //var cantidad: Int = 0,
    //var user: String = "",
    //var useridcard: String = "",

    val card_sets: List<CardSet>? = null,
    val card_images: List<CardImage>? = null,
    val card_prices: List<CardPrice>? = null


):Parcelable
data class CardResponse(
    val data: List<CardItem>
)

@Parcelize
data class CardSet(
    val set_name: String,
    val set_code: String,
    val set_rarity: String,
    val set_rarity_code: String,
    val set_price: String
):Parcelable

@Parcelize
data class CardImage(
    val id: Int,
    val image_url: String,
    val image_url_small: String,
    val image_url_cropped: String,
    var isSelected: Boolean = false // Agregar esta propiedad
):Parcelable

@Parcelize
data class CardPrice(
    val cardmarket_price: String,
    val tcgplayer_price: String,
    val ebay_price: String,
    val amazon_price: String,
    val coolstuffinc_price: String
):Parcelable