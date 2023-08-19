package com.slaytertv.tcgbolivia.ui.adapters.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.databinding.ItemCartasellBinding
import java.text.SimpleDateFormat

class HomeCardSellAdapter  (
    //creo variable para cuando editen, borren, el item
    val onItemClicked: (Int, CardItem) -> Unit,
    //val onEditClicked: (Int, NoteItem) -> Unit,
    //val onDeleteClicked: (Int,NoteItem) -> Unit
) : RecyclerView.Adapter<HomeCardSellAdapter.MyViewHolder>() {

    val sdf = SimpleDateFormat("dd MM yyyy")
    //creo lista mutable para que contenga todos los datos de mi noteitem
    private var list: MutableList<CardItem> = arrayListOf()
    //viewholder que llama a mi itemnotelayotu.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemCartasellBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }
    //posicion de los items
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }
    //muestra los cambios de mi recyclerview
    fun updateList(list: MutableList<CardItem>){
        this.list = list
        notifyDataSetChanged()
    }
    //remueve items del recyclerview y muestra los cambios
    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemChanged(position)
    }
    //cantidad de items
    override fun getItemCount(): Int {
        return list.size
    }
    //ccontinuacion del viewhoklder utilizando los botones dek xml y agregando datos
    inner class MyViewHolder(val binding: ItemCartasellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CardItem){
            binding.selltextName.setText(item.name)
            Glide.with(binding.root).load("https://images.ygoprodeck.com/images/cards_small/${item.id}.jpg") .transform(
                RoundedCorners(25)
            ).into(binding.Imagesell)
            binding.selltextPrecio.setText(item.precio.toString())
            binding.selltextCantidad.setText(item.cantidad.toString())
            binding.selltextCantidad.setOnClickListener { onItemClicked.invoke(adapterPosition,item) }
        }
    }
}