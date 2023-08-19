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
class HomeCardsBuyViewModel  @Inject constructor(
    //4 crear variable para tener a noterepository
    val repository: CardsRepository
): ViewModel() {
    //creamos una variable privada para la lista mutable en vivo con los items que llegan por noteitem
    private val _cardbuy = MutableLiveData<UiState<List<CardItem>>>()
    //variable note con los items de noteitem
    val cardbuy: LiveData<UiState<List<CardItem>>>
        get() = _cardbuy
    private val _updateCardbuy = MutableLiveData<UiState<String>>()
    val updateCardBuy: LiveData<UiState<String>>
        get() = _updateCardbuy
    ///////////////////////////////////////////////////////////////
//crear funcion con los items que se devolveran a getnote solo si es de nuestro usuario
    fun getCardBuy() {
        _cardbuy.value = UiState.Loading
        //a obtener notes le mandamkos nuestro usuario
        repository.getCardBuy() { _cardbuy.value = it }
    }
    //funcion para catualiizar
    fun updateCardBuy(cardbuy:CardItem){
        _updateCardbuy.value = UiState.Loading
        repository.updateCardBuy(cardbuy) { _updateCardbuy.value = it}
    }

}