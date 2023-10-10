package com.slaytertv.tcgbolivia.ui.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.data.repository.CardsRepository
import com.slaytertv.tcgbolivia.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeCardsTopViewModel   @Inject constructor(
    //4 crear variable para tener a noterepository
    val repository: CardsRepository
): ViewModel() {
    //creamos una variable privada para la lista mutable en vivo con los items que llegan por noteitem
    private val _cardtop = MutableLiveData<UiState<List<CardDosItem>>>()
    //variable note con los items de noteitem
    val cardtop: LiveData<UiState<List<CardDosItem>>>
        get() = _cardtop
    private val _updateCardtop = MutableLiveData<UiState<String>>()
    val updateCardTop: LiveData<UiState<String>>
        get() = _updateCardtop
    ///////////////////////////////////////////////////////////////
//crear funcion con los items que se devolveran a getnote solo si es de nuestro usuario
    fun getCardTop() {
        _cardtop.value = UiState.Loading
        //a obtener notes le mandamkos nuestro usuario
        repository.getCardTop() { _cardtop.value = it }
    }
    //funcion para catualiizar
    fun updateCardTop(cardtop: CardItem){
        _updateCardtop.value = UiState.Loading
        repository.updateCardTop(cardtop) { _updateCardtop.value = it}
    }

}