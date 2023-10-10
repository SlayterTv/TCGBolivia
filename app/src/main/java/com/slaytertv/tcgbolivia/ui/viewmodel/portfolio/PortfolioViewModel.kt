package com.slaytertv.tcgbolivia.ui.viewmodel.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.data.repository.CardsRepository
import com.slaytertv.tcgbolivia.data.repository.YgoProDeckApiServiceImp
import com.slaytertv.tcgbolivia.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val repository: YgoProDeckApiServiceImp,
    val repositoryfirebase: CardsRepository
) : ViewModel() {

    private val _cardsLiveData = MutableLiveData<List<CardItem>?>()
    val cardsLiveData: LiveData<List<CardItem>?> get() = _cardsLiveData

    fun searchCards(partialName: String?) {
        viewModelScope.launch {
            try {
                val cards = repository.searchCards(partialName)
                _cardsLiveData.value = cards // Asignar el valor directamente
            } catch (e: Exception) {
                // Manejar el error si hay una excepción
            }
        }
    }
    ////
//    registrar venta
    private val _registersell = MutableLiveData<UiState<String>>()
    //para ver los datos actuales
    val registersell: LiveData<UiState<String>>
        get() = _registersell

    //funcion cuando se registren
    fun registersell(
        card: CardDosItem
    ) {
        _registersell.value = UiState.Loading
        repositoryfirebase.createCardSell(
            card = card
        ) { _registersell.value = it }
    }
}
