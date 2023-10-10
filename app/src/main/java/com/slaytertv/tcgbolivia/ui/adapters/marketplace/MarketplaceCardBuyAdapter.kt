package com.slaytertv.tcgbolivia.ui.adapters.marketplace

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.databinding.ListItemCardbuyBinding

class MarketplaceCardBuyAdapter (
    val onItemClicked: (Int, CardDosItem) -> Unit,
): ListAdapter<CardDosItem, MarketplaceCardBuyAdapter.CardViewHolder>(CardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCardbuyBinding.inflate(inflater, parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = getItem(position)
        holder.bind(card)
    }

    inner class CardViewHolder(private val binding: ListItemCardbuyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(card: CardDosItem) {
            // Actualiza las vistas en el layout de list_item_card.xml directamente
            binding.cardbuyNameTextView.text = card.name
            binding.cardbuyIdTextView.text = card.id.toString()

            binding.cardbuytextSetname.setText(card.set_name)
            binding.cardbuytextSetcode.setText(card.set_code)
            binding.cardbuytextSetrarity.setText(card.set_rarity)
            binding.cardbuytextSetraritycode.setText(card.set_rarity_code).toString()
            binding.cardbuytextPrecio.setText(card.precio.toString())
            binding.cardbuytextCantidad.setText(card.cantidad.toString())




            Glide.with(itemView.context)
                .load(card.imageUrl) // Reemplaza card.imageUrl con la URL real de la imagen
                .into(binding.cardbuyImage)
            // Bind otras vistas según sea necesario
            binding.cardbuysaveitem.setOnClickListener {
                val context = binding.cardbuysaveitem.context
                val resultado = verificaranonimo()
                if(resultado == "Usuario anónimo"){
                    Toast.makeText(context, "Cree una cuenta para ${resultado} ", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                //val auth = FirebaseAuth.getInstance()
               // val currentUser = auth.currentUser
                //card.user = currentUser!!.uid
                onItemClicked.invoke(adapterPosition,card)
            }
        }

    }

}

//posicion del item
class CardDiffCallback : DiffUtil.ItemCallback<CardDosItem>() {
    override fun areItemsTheSame(oldItem: CardDosItem, newItem: CardDosItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CardDosItem, newItem: CardDosItem): Boolean {
        return oldItem == newItem
    }
}

//verificar anonimo
private fun verificaranonimo(): String {
    lateinit var auth: FirebaseAuth
    auth = FirebaseAuth.getInstance()
    val currentUser: FirebaseUser? = auth.currentUser
    return if (currentUser != null && currentUser.isAnonymous) {
        "Usuario anónimo"
    } else {
        "Usuario autenticado"
    }
}
