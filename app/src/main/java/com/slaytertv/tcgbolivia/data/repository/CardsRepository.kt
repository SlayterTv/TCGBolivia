package com.slaytertv.tcgbolivia.data.repository

import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.util.UiState

interface CardsRepository {
    //buy
    fun createCardBuy(card: CardItem, result: (UiState<String>) -> Unit)
    fun getCardBuy(result: (UiState<List<CardItem>>) -> Unit)
    fun updateCardBuy(cardbuy: CardItem, result: (UiState<String>) -> Unit)
    //sell
    fun getCardSell(result: (UiState<List<CardItem>>) -> Unit)
    fun updateCardSell(cardsell: CardItem, result: (UiState<String>) -> Unit)
    //top
    fun getCardTop(result: (UiState<List<CardItem>>) -> Unit)
    fun updateCardTop(cardtop: CardItem, result: (UiState<String>) -> Unit)
}