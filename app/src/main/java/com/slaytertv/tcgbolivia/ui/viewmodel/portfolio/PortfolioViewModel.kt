package com.slaytertv.tcgbolivia.ui.viewmodel.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.data.repository.YgoProDeckApiServiceImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(private val repository: YgoProDeckApiServiceImp) : ViewModel() {

    private val _cardsLiveData = MutableLiveData<List<CardItem>?>()
    val cardsLiveData: LiveData<List<CardItem>?> get() = _cardsLiveData

    // Agregar una lista de URLs de imágenes de las cartas
    private val _cardImageUrls = MutableLiveData<List<String>>()
    val cardImageUrls: LiveData<List<String>> get() = _cardImageUrls
    fun searchCards(partialName: String?) {
        viewModelScope.launch {
            try {
                val cards = repository.searchCards(partialName)
                _cardsLiveData.value = cards // Asignar el valor directamente
                // Actualizar las URLs de imágenes de las cartas
                _cardImageUrls.value = cards!!.map { it.imageUrl }
            } catch (e: Exception) {
                // Manejar el error si hay una excepción
            }
        }
    }
}
