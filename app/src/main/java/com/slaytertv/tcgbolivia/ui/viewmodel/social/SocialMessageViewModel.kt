package com.slaytertv.tcgbolivia.ui.viewmodel.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slaytertv.tcgbolivia.data.model.ChatMessageItem
import com.slaytertv.tcgbolivia.data.repository.CardsRepository
import com.slaytertv.tcgbolivia.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SocialMessageViewModel @Inject constructor(
    //4 crear variable para tener a noterepository
    val repository: CardsRepository
): ViewModel() {
    //obtenemos los mensajes
    private val _getmessages = MutableLiveData<UiState<List<ChatMessageItem>>>()
    val getmessages: LiveData<UiState<List<ChatMessageItem>>>
        get() = _getmessages
    fun getMessages(chatmessage:String) {
        _getmessages.value = UiState.Loading
        //a obtener notes le mandamkos nuestro usuario
        repository.getChatMessages(
            chatId = chatmessage
        ) { _getmessages.value = it }
    }

    /////////////agregar mensaje
    //
    private val _addmessage = MutableLiveData<UiState<ChatMessageItem>>()
    val addmessage: LiveData<UiState<ChatMessageItem>>
        get() = _addmessage
    //funcion cuando se registren
    fun addMessage(
        id:String,
        card: ChatMessageItem
    ) {
        _addmessage.value = UiState.Loading
        repository.addMessageToChat(
            id,
            chatId = card
        ) { _addmessage.value = it }
    }
}