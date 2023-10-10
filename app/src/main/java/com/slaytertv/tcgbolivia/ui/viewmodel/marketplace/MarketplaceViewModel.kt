package com.slaytertv.tcgbolivia.ui.viewmodel.marketplace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.data.repository.CardsRepository
import com.slaytertv.tcgbolivia.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    val repository: CardsRepository
) : ViewModel() {

    private val _cardsLiveData = MutableLiveData<List<CardDosItem>?>()
    val cardsLiveData: LiveData<List<CardDosItem>?> get() = _cardsLiveData

   fun searchCards(partialName: String) {
       viewModelScope.launch {
           val searchResults = repository.searchCardsByName(partialName)
           _cardsLiveData.postValue(searchResults)
       }
    }
    ////

    private val _registerbuy = MutableLiveData<UiState<String>>()
    //para ver los datos actuales
    val registerbuy: LiveData<UiState<String>>
        get() = _registerbuy

    //funcion cuando se registren
    fun registerbuy(
        card: CardDosItem
    ) {
        _registerbuy.value = UiState.Loading
        repository.createCardBuy(
            card = card
        ) { _registerbuy.value = it }
    }
}
