package com.slaytertv.tcgbolivia.ui.adapters.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.databinding.ItemCartasellBinding
import java.text.SimpleDateFormat

class HomeCardSellAdapter  (
    //creo variable para cuando editen, borren, el item
    val onItemClicked: (Int, CardDosItem) -> Unit,
    //val onEditClicked: (Int, NoteItem) -> Unit,
    //val onDeleteClicked: (Int,NoteItem) -> Unit
) : RecyclerView.Adapter<HomeCardSellAdapter.MyViewHolder>() {

    val sdf = SimpleDateFormat("dd MM yyyy")
    //creo lista mutable para que contenga todos los datos de mi noteitem
    private var list: MutableList<CardDosItem> = arrayListOf()
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
    fun updateList(list: MutableList<CardDosItem>){
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
        fun bind(item: CardDosItem){
            binding.selltextName.setText(item.name)
            Glide.with(binding.root).load(item.imageUrl) .transform(
                RoundedCorners(25)
            ).into(binding.Imagesell)
            binding.selltextSetname.setText(item.set_name)
            binding.selltextSetcode.setText(item.set_code)
            binding.selltextSetraritycode.setText(item.set_rarity)
            binding.selltextSetprice.setText(item.set_price)
            binding.selltextPrecio.setText(item.precio.toString())
            binding.selltextCantidad.setText(item.cantidad.toString())
            binding.selltextCantidad.setOnClickListener { onItemClicked.invoke(adapterPosition,item) }
        }
    }
}