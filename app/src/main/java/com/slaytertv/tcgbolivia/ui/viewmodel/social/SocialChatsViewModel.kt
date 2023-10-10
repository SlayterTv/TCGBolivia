package com.slaytertv.tcgbolivia.ui.viewmodel.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slaytertv.tcgbolivia.data.model.RutaChatItem
import com.slaytertv.tcgbolivia.data.repository.CardsRepository
import com.slaytertv.tcgbolivia.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SocialChatsViewModel   @Inject constructor(
    //4 crear variable para tener a noterepository
    val repository: CardsRepository
): ViewModel() {
    //creamos una variable privada para la lista mutable en vivo con los items que llegan por noteitem
    private val _getchats = MutableLiveData<UiState<List<RutaChatItem>>>()
    //variable note con los items de noteitem
    val getchats: LiveData<UiState<List<RutaChatItem>>>
        get() = _getchats
    ///////////////////////////////////////////////////////////////
//crear funcion con los items que se devolveran a getnote solo si es de nuestro usuario
    fun getChats() {
        _getchats.value = UiState.Loading
        //a obtener notes le mandamkos nuestro usuario
        repository.getChatsForCurrentUser() { _getchats.value = it }
    }
}