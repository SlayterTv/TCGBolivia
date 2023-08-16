

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.databinding.ListItemCardBinding


class CardsAdapter : ListAdapter<CardItem, CardsAdapter.CardViewHolder>(CardDiffCallback()) {

    private var cardImageUrls: List<String> = emptyList()

    fun setImageUrls(urls: List<String>) {
        cardImageUrls = urls
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCardBinding.inflate(inflater, parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = getItem(position)
        holder.bind(card)
    }

    inner class CardViewHolder(private val binding: ListItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(card: CardItem) {
            // Actualiza las vistas en el layout de list_item_card.xml directamente
            binding.cardNameTextView.text = card.name
            binding.cardIdTextView.text = card.id.toString()
            Glide.with(itemView.context)
                .load("https://images.ygoprodeck.com/images/cards_small/${card.id.toString()}.jpg") // Reemplaza card.imageUrl con la URL real de la imagen
                .into(binding.cardImage)
            // Bind otras vistas según sea necesario
            binding.cardsaveitem.setOnClickListener {
                val cantidadText = binding.cardCantidadEditText.text.toString()
                val precioText = binding.cardPrecioEditText.text.toString()

                val context = binding.cardsaveitem.context
                if(precioText.isNullOrEmpty() ){
                    Toast.makeText(context, "Agregue Precio de Venta", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(cantidadText.isNullOrEmpty()){
                    Toast.makeText(context, "Agregue una Cantidad de cartas", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val resultado = verificaranonimo()
                if(resultado == "Usuario anónimo"){
                    Toast.makeText(context, "Cree una cuenta para ${resultado} ", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                val cantidad = cantidadText.toInt()
                val precio = precioText.toInt()
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                card.cantidad = cantidad
                card.precio = precio
                card.user = currentUser!!.uid
                card.useridcard = "${currentUser!!.uid}_${card.id}"
                guardardatosventa(card,context)
            }
        }

    }

}

//posicion del item
class CardDiffCallback : DiffUtil.ItemCallback<CardItem>() {
    override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
        return oldItem == newItem
    }
}
//guardarventa y datos en nuestro user
fun guardardatosventa(card: CardItem,xcontext:Context){
    val firestore = FirebaseFirestore.getInstance()
    val collectionRef = firestore.collection("Ventas").document(card.useridcard)
    val collectionuserRef = firestore.collection("user").document(card.user).collection("venta").document(card.useridcard)
    // Get a new write batch and commit all write operations
    firestore.runBatch { batch ->
        // Set the value of 'NYC'
        batch.set(collectionuserRef, card)
        batch.set(collectionRef, card)
    }.addOnCompleteListener {
        Toast.makeText(xcontext, "Carta lista para la venta", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener { e ->
        Toast.makeText(xcontext, "Error al guardar la carta: ${e.message}", Toast.LENGTH_SHORT).show()
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
