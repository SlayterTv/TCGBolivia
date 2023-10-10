

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.databinding.ListItemCardBinding


class CardsAdapter (
    val onItemClicked: (Int, CardItem) -> Unit,
): ListAdapter<CardItem, CardsAdapter.CardViewHolder>(CardDiffCallback()) {

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
            Glide.with(itemView.context)
                .load("https://images.ygoprodeck.com/images/cards_small/${card.id.toString()}.jpg") // Reemplaza card.imageUrl con la URL real de la imagen
                .into(binding.cardImage)
            // Bind otras vistas según sea necesario
            binding.cardLinear.setOnClickListener {
                val context = binding.cardLinear.context

                val resultado = verificaranonimo()
                if(resultado == "Usuario anónimo"){
                    Toast.makeText(context, "Cree una cuenta para ${resultado} ", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                onItemClicked.invoke(adapterPosition,card)
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
