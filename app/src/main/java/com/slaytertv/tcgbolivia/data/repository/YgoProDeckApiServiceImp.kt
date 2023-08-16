package com.slaytertv.tcgbolivia.data.repository

import com.slaytertv.tcgbolivia.data.model.CardItem
import javax.inject.Inject

class YgoProDeckApiServiceImp  @Inject constructor(private val apiService: YgoProDeckApiService) {

    suspend fun searchCards(partialName: String?): List<CardItem>? {
        val response = apiService.getCardsByPartialName(partialName)
        if (response.isSuccessful) {
            val cardResponse = response.body()
            return cardResponse?.data
        }
        return null
    }
}