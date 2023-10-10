package com.slaytertv.tcgbolivia.ui.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.data.repository.CardsRepository
import com.slaytertv.tcgbolivia.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailBuyViewModel @Inject constructor(
    val repository: CardsRepository
) : ViewModel() {

    //
    private val _registerchat = MutableLiveData<UiState<String>>()
    val registerchat: LiveData<UiState<String>>
        get() = _registerchat
    //funcion cuando se registren
    fun registerchat(
        card: CardDosItem
    ) {
        _registerchat.value = UiState.Loading
        repository.createfirstchat(
            card = card
        ) { _registerchat.value = it }
    }









}