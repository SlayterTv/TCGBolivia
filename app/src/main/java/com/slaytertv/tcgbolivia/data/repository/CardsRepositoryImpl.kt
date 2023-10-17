package com.slaytertv.tcgbolivia.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.data.model.ChatMessageItem
import com.slaytertv.tcgbolivia.data.model.RutaChatItem
import com.slaytertv.tcgbolivia.util.UiState
import kotlinx.coroutines.tasks.await

class CardsRepositoryImpl (
    //uasremos auth
    val auth: FirebaseAuth,
    //y firestore
    val database: FirebaseFirestore,
    val databaseD: FirebaseDatabase
    // Inyecta Firebase Firestore u otra dependencia necesaria
) : CardsRepository {
    override fun createCardBuy(card: CardDosItem, result: (UiState<String>) -> Unit) {
        val collectionuserRef = database.collection("user").document(auth.currentUser!!.uid).collection("compras").document(card.useridcard)
        // Get a new write batch and commit all write operations
        database.runBatch { batch ->
            // Set the value of 'NYC'
            batch.set(collectionuserRef, card)
        }.addOnCompleteListener {
            result.invoke( UiState.Sucess("Se guardo correctamente") )
        }.addOnFailureListener { e ->
            result.invoke( UiState.Failure("Error al guardar la carta: ${e.message}") )
        }
    }

    override fun getCardBuy( result: (UiState<List<CardDosItem>>) -> Unit) {
        val currentUser: FirebaseUser? = auth.currentUser
        database.collection("user").document(currentUser!!.uid).collection("compras")
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<CardDosItem>()
                for (document in it){
                    val note = document.toObject(CardDosItem::class.java)
                    note.idcard = document.id

                    notes.add(note)
                }
                //invocamos el result para pasar los datos de completado
                result.invoke(UiState.Sucess(notes))
            }
            //si falla llamamos a resulta pasando el mensaje con el error que ocurrio
            .addOnFailureListener { result.invoke(UiState.Failure(it.localizedMessage)) }
    }

    override fun updateCardBuy(cardbuy: CardItem, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }


    override fun createCardSell(card: CardDosItem, result: (UiState<String>) -> Unit) {
        card.user = auth.currentUser!!.uid
        card.useridcard = "${auth.currentUser!!.uid}${card.idcard}${card.set_code}"
        ///firebastore
        //val collectionRef = database.collection("Ventas").document(card.useridcard)
        val collectionuserRef = database.collection("user").document(card.user).collection("venta").document(card.useridcard)
        // Get a new write batch and commit all write operations
        database.runBatch { batch ->
            // Set the value of 'NYC'
            batch.set(collectionuserRef, card)
          //  batch.set(collectionRef, card)
        }.addOnCompleteListener {
            createCardSellD(card,result)
        }.addOnFailureListener { e ->
            result.invoke( UiState.Failure("Error al guardar la carta: ${e.message}") )
        }

    }
    private fun createCardSellD(card:CardDosItem, result: (UiState<String>) -> Unit){
        ////database
        val salesReference: DatabaseReference = databaseD.reference.child("ventas")
        val cardName = "${card.user}${card.idcard}${card.set_code}" // Cambiar esto por el nombre que desees

        val newSalesItemRef = salesReference.child(cardName)
        newSalesItemRef.setValue(card)
            .addOnSuccessListener {
                result.invoke(UiState.Sucess("Carta lista para la venta!"))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure("Error al guardar la carta: ${e.message}"))
            }
    }
    override fun getCardSell(result: (UiState<List<CardDosItem>>) -> Unit) {
        val currentUser: FirebaseUser? = auth.currentUser
        database.collection("user").document(currentUser!!.uid.toString()).collection("venta")
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<CardDosItem>()
                for (document in it){
                    val note = document.toObject(CardDosItem::class.java)
                    note.idcard = document.id

                    notes.add(note)
                }
                //invocamos el result para pasar los datos de completado
                result.invoke(UiState.Sucess(notes))
            }
            //si falla llamamos a resulta pasando el mensaje con el error que ocurrio
            .addOnFailureListener { result.invoke(UiState.Failure(it.localizedMessage)) }
    }

    override fun updateCardSell(cardsell: CardItem, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getCardTop(result: (UiState<List<CardDosItem>>) -> Unit) {
       database.collection("top")
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<CardDosItem>()
                for (document in it){
                    val note = document.toObject(CardDosItem::class.java)
                    note.idcard = document.id
                    notes.add(note)
                }
                //invocamos el result para pasar los datos de completado
                result.invoke(UiState.Sucess(notes))
            }
            //si falla llamamos a resulta pasando el mensaje con el error que ocurrio
            .addOnFailureListener { result.invoke(UiState.Failure(it.localizedMessage)) }
    }

    override fun updateCardTop(cardtop: CardItem, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }



    override suspend fun searchCardsByName(nameQuery: String,cuac:String): List<CardDosItem> {
        val cardsReference: DatabaseReference = databaseD.reference.child("ventas")

        val query = cardsReference.orderByChild("name")
            .startAt(nameQuery)
            .endAt(nameQuery + "\uf8ff") // Para incluir todas las posibles combinaciones de caracteres

        val dataSnapshot = query.get().await()

        val cardItems = mutableListOf<CardDosItem>()
        for (childSnapshot in dataSnapshot.children) {
            val card = childSnapshot.getValue(CardDosItem::class.java)
            card?.let {
                if(it.juego == cuac){
                    cardItems.add(it)
                }
            }
        }

        return cardItems
    }

    override fun createfirstchat(card: CardDosItem, result: (UiState<String>) -> Unit) {
        // Supongamos que tienes las siguientes variables:
        val currentUser = auth.currentUser!!.uid // El usuario actual que inicia el chat
        val otherUser = card.user // El otro usuario con el que se inicia el chat
        val cardId = card.idcard
        val chatsRef = databaseD.reference.child("chats")

        // Combinar UIDs para crear un chat único
        val chatId = "${currentUser}_${otherUser}_${cardId}"

// Verificar si el chat único ya existe
        chatsRef.child(chatId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // El chat ya existe, puedes abrirlo o realizar alguna acción
                    // Puedes obtener la referencia del chat existente si es necesario
                    val chatReference = dataSnapshot.ref
                } else {
                    // El chat no existe, créalo
                    val chatData = HashMap<String, Any>()
                    // Configura otros datos relevantes del chat si es necesario
                    chatData["participants"] = mapOf(currentUser to true, otherUser to true)
                    chatData["detalles"] = mapOf("namecard" to card.name, "imagen" to card.imageUrl, "setcode" to card.set_code)

                    // Crea el chat único
                    chatsRef.child(chatId).updateChildren(chatData)
                        .addOnSuccessListener {
                            // El chat se creó con éxito, puedes abrirlo o realizar alguna acción
                            val chatReference = chatsRef.child(currentUser).child(otherUser).child(cardId)
                            result.invoke(UiState.Sucess("ingrese a menu Social para iniciar el chat"))

                        }
                        .addOnFailureListener { e ->
                            // Ocurrió un error al crear el chat
                            // Maneja el error según sea necesario
                            result.invoke(UiState.Failure("Error al guardar la carta: ${e.message}") )
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja los errores si es necesario
                result.invoke(UiState.Failure("se cancelo"))
            }
        })
    }

    override fun getChatsForCurrentUser(result: (UiState<List<RutaChatItem>>) -> Unit) {
        val currentUser = auth.currentUser!!.uid
        val database = FirebaseDatabase.getInstance()
        val chatsRef = database.reference.child("chats")

        // Consulta los chats en los que currentUserUid es un participante
        chatsRef.orderByChild("participants/$currentUser").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val chatDetailsList = mutableListOf<RutaChatItem>()
                    for (chatSnapshot in dataSnapshot.children) {
                        val participantsSnapshot = chatSnapshot.child("participants")

                        // Verifica si otherUserUid está en los participantes
                        if (participantsSnapshot.hasChild(currentUser)) {
                            // El usuario tiene acceso a este chat, puedes recuperar los detalles
                            val detailsSnapshot = chatSnapshot.child("detalles")
                            val chatName = "${detailsSnapshot.child("namecard").getValue(String::class.java)}_${detailsSnapshot.child("setcode").getValue(String::class.java)}"
                            val chatImage = detailsSnapshot.child("imagen").getValue(String::class.java)
                            val chatRuta = chatSnapshot.key.toString()

                            // Crea un objeto ChatDetails y agrégalo a la lista
                            val chatDetails = RutaChatItem(0,chatName, chatImage.toString(),chatRuta)
                            chatDetailsList.add(chatDetails)
                        }
                    }
                    // Llama a la devolución de llamada con la lista de detalles de chat
                    result.invoke(UiState.Sucess(chatDetailsList))
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar errores si es necesario
                }
            })
    }

    override fun getChatsForOtherUser(otherUserUid: String,result: (UiState<List<RutaChatItem>>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val chatsRef = database.reference.child("chats")

        // Consulta los chats en los que currentUserUid es un participante
        chatsRef.orderByChild("participants/$otherUserUid").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val chatDetailsList = mutableListOf<RutaChatItem>()
                    for (chatSnapshot in dataSnapshot.children) {
                        val participantsSnapshot = chatSnapshot.child("participants")

                        // Verifica si otherUserUid está en los participantes
                        if (participantsSnapshot.hasChild(otherUserUid)) {
                            // El usuario tiene acceso a este chat, puedes recuperar los detalles
                            val detailsSnapshot = chatSnapshot.child("detalles")
                            val chatName = "${detailsSnapshot.child("namecard").getValue(String::class.java)}_${detailsSnapshot.child("setcode").getValue(String::class.java)}"
                            val chatImage = detailsSnapshot.child("imagen").getValue(String::class.java)

                            // Crea un objeto ChatDetails y agrégalo a la lista
                            val chatDetails = RutaChatItem(0,chatName, chatImage.toString(),"")
                            chatDetailsList.add(chatDetails)
                        }
                    }
                    // Llama a la devolución de llamada con la lista de detalles de chat
                    result.invoke(UiState.Sucess(chatDetailsList))

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar errores si es necesario
                }
            })
    }

    override fun getChatMessages(chatId: String, result: (UiState<List<ChatMessageItem>>) -> Unit) {
        val chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages")
        val currentUser = auth.currentUser!!.uid

        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messages = mutableListOf<ChatMessageItem>()

                for (messageSnapshot in dataSnapshot.children) {
                    val senderId = messageSnapshot.child("senderId").getValue(String::class.java)
                    val text = messageSnapshot.child("text").getValue(String::class.java)
                    val timestamp = messageSnapshot.child("timestamp").getValue(Long::class.java)

                    if (senderId != null && text != null && timestamp != null) {
                        val chatMessageItem = ChatMessageItem(senderId, text, timestamp,currentUser)
                        messages.add(chatMessageItem)
                    }
                }

                // Ordena los mensajes por su marca de tiempo (timestamp)
                messages.sortBy { it.timestamp }

                // Llama a la devolución de llamada con la lista de mensajes ordenados
                //callback(messages)
                result.invoke(UiState.Sucess(messages))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja los errores aquí si es necesario
            }
        })
    }

    override fun addMessageToChat(
        chatIdx: String,
        chatId: ChatMessageItem,
        result: (UiState<ChatMessageItem>) -> Unit
    ) {
        val currentUser = auth.currentUser!!.uid
        // Obtiene una referencia a la ubicación del chat en Firebase Database
        val chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatIdx)
        val timestamp = System.currentTimeMillis()
        // Crea un nuevo mensaje con la información necesaria
        val message = ChatMessageItem(currentUser, chatId.text, timestamp)

        // Agrega el mensaje al chat utilizando un nuevo ID único
        val messageIdx = chatRef.child("messages").push().key
        chatRef.child("messages").child(messageIdx!!).setValue(message)
            .addOnSuccessListener {
                result.invoke(UiState.Sucess(message))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure("error $e"))
            }
    }
}