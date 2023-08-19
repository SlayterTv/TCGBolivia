package com.slaytertv.tcgbolivia.ui.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.data.repository.CardsRepository
import com.slaytertv.tcgbolivia.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeCardsSellViewModel   @Inject constructor(
    //4 crear variable para tener a noterepository
    val repository: CardsRepository
): ViewModel() {
    //creamos una variable privada para la lista mutable en vivo con los items que llegan por noteitem
    private val _cardsell = MutableLiveData<UiState<List<CardItem>>>()
    //variable note con los items de noteitem
    val cardsell: LiveData<UiState<List<CardItem>>>
        get() = _cardsell
    private val _updateCardsell = MutableLiveData<UiState<String>>()
    val updateCardSell: LiveData<UiState<String>>
        get() = _updateCardsell
    ///////////////////////////////////////////////////////////////
//crear funcion con los items que se devolveran a getnote solo si es de nuestro usuario
    fun getCardSell() {
        _cardsell.value = UiState.Loading
        //a obtener notes le mandamkos nuestro usuario
        repository.getCardSell() { _cardsell.value = it }
    }
    //funcion para catualiizar
    fun updateCardSell(cardsell: CardItem){
        _updateCardsell.value = UiState.Loading
        repository.updateCardSell(cardsell) { _updateCardsell.value = it}
    }

}