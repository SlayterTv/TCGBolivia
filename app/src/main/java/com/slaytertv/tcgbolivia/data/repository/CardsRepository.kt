package com.slaytertv.tcgbolivia.data.repository

import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.data.model.ChatMessageItem
import com.slaytertv.tcgbolivia.data.model.RutaChatItem
import com.slaytertv.tcgbolivia.util.UiState

interface CardsRepository {
    //buy

    fun createCardBuy(card: CardDosItem, result: (UiState<String>) -> Unit)
    fun getCardBuy(result: (UiState<List<CardDosItem>>) -> Unit)
    fun updateCardBuy(cardbuy: CardItem, result: (UiState<String>) -> Unit)
    //sell
    fun createCardSell(card: CardDosItem, result: (UiState<String>) -> Unit)
    fun getCardSell(result: (UiState<List<CardDosItem>>) -> Unit)
    fun updateCardSell(cardsell: CardItem, result: (UiState<String>) -> Unit)
    //top
    fun getCardTop(result: (UiState<List<CardDosItem>>) -> Unit)
    fun updateCardTop(cardtop: CardItem, result: (UiState<String>) -> Unit)
    //
    suspend fun searchCardsByName(nameQuery: String): List<CardDosItem>

    //

    fun createfirstchat(card:CardDosItem, result: (UiState<String>) -> Unit)
    //
    fun getChatsForCurrentUser(result: (UiState<List<RutaChatItem>>) -> Unit)
    fun getChatsForOtherUser(otherUserUid: String,result: (UiState<List<RutaChatItem>>) -> Unit)
    fun getChatMessages(chatId: String,result: (UiState<List<ChatMessageItem>>) -> Unit)
    fun addMessageToChat(chatIdx:String,chatId: ChatMessageItem,result: (UiState<ChatMessageItem>) -> Unit)

    }