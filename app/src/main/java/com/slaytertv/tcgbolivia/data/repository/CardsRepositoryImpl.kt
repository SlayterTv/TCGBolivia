package com.slaytertv.tcgbolivia.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.util.UiState

class CardsRepositoryImpl (
    //uasremos auth
    val auth: FirebaseAuth,
    //y firestore
    val database: FirebaseFirestore,
    val databaseD: FirebaseDatabase
    // Inyecta Firebase Firestore u otra dependencia necesaria
) : CardsRepository {
    override fun createCardBuy(card: CardItem, result: (UiState<String>) -> Unit) {
        ///firebastore
        val collectionRef = database.collection("Ventas").document(card.useridcard)
        val collectionuserRef = database.collection("user").document(card.user).collection("venta").document(card.useridcard)
        // Get a new write batch and commit all write operations
        database.runBatch { batch ->
            // Set the value of 'NYC'
            batch.set(collectionuserRef, card)
            batch.set(collectionRef, card)
        }.addOnCompleteListener {
            createCardBuyD(card,result)
        }.addOnFailureListener { e ->
            result.invoke( UiState.Failure("Error al guardar la carta: ${e.message}") )
        }

    }
    private fun createCardBuyD(card:CardItem, result: (UiState<String>) -> Unit){
        ////database
        val salesReference: DatabaseReference = databaseD.reference.child("ventas").child(card.useridcard)
        val newSalesItemRef = salesReference.push()
        newSalesItemRef.setValue(card)
            .addOnSuccessListener {
                result.invoke( UiState.Sucess("Carta lista para la venta!") )
            }
            .addOnFailureListener { e ->
                result.invoke( UiState.Failure("Error al guardar la carta: ${e.message}") )
            }
    }





    override fun getCardBuy( result: (UiState<List<CardItem>>) -> Unit) {
        val currentUser: FirebaseUser? = auth.currentUser
        database.collection("user").document(currentUser!!.uid.toString()).collection("venta")
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<CardItem>()
                for (document in it){
                    val note = document.toObject(CardItem::class.java)
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

    override fun getCardSell(result: (UiState<List<CardItem>>) -> Unit) {
        val currentUser: FirebaseUser? = auth.currentUser
        database.collection("user").document(currentUser!!.uid.toString()).collection("venta")
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<CardItem>()
                for (document in it){
                    val note = document.toObject(CardItem::class.java)
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

    override fun getCardTop(result: (UiState<List<CardItem>>) -> Unit) {
        val currentUser: FirebaseUser? = auth.currentUser
        database.collection("user").document(currentUser!!.uid.toString()).collection("venta")
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<CardItem>()
                for (document in it){
                    val note = document.toObject(CardItem::class.java)
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
}