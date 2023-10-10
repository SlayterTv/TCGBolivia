package com.slaytertv.tcgbolivia.ui.adapters.portfolio

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slaytertv.tcgbolivia.data.model.CardImage
import com.slaytertv.tcgbolivia.databinding.ItemCardImageBinding

class CardImagesAdapter(private val cardImages: List<CardImage>) :
    RecyclerView.Adapter<CardImagesAdapter.ViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    private var imageClickListener: ((String) -> Unit)? = null
    init {
        selectedPosition = RecyclerView.NO_POSITION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardImageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardImage = cardImages[position]
        holder.bind(cardImage)
    }

    override fun getItemCount(): Int = cardImages.size

    inner class ViewHolder(private val binding: ItemCardImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cardImage: CardImage) {
            // Cargar la imagen usando una biblioteca de imágenes como Glide o Picasso
            Glide.with(binding.root)
                .load(cardImage.image_url_small)
                .into(binding.cardImageView)

            // Configurar el click listener para la imagen

            // Aplicar filtro de color y efecto de selección
            if (cardImage.isSelected) {
                imageClickListener?.invoke(cardImage.image_url_small)
                binding.cardImageView.setColorFilter(Color.parseColor("#8000FF00"), PorterDuff.Mode.SRC_OVER)
                binding.root.alpha = 1f
            } else {
                binding.cardImageView.clearColorFilter()
                binding.root.alpha = 0.5f
            }

            // Configurar el click listener para la imagen
            binding.root.setOnClickListener {
                val previousSelectedPosition = selectedPosition
                selectedPosition = adapterPosition
                if (previousSelectedPosition != RecyclerView.NO_POSITION) {
                    cardImages[previousSelectedPosition].isSelected = false
                }
                cardImage.isSelected = true
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(adapterPosition)
            }



        }
    }

    // Función para configurar el listener de la selección de imagen
    fun setOnImageClickListener(listener: (String) -> Unit) {
        imageClickListener = listener
    }
}
