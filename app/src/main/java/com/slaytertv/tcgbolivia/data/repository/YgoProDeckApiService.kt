package com.slaytertv.tcgbolivia.data.repository


import com.slaytertv.tcgbolivia.data.model.CardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface YgoProDeckApiService {
    //buscar por todas las cartas
    @GET("cardinfo.php")
    suspend fun getAllCards(): Response<CardResponse>

    //por nombre
    @GET("cardinfo.php")
    suspend fun getCardsByPartialName(@Query("fname") partialName: String?): Response<CardResponse>


}
