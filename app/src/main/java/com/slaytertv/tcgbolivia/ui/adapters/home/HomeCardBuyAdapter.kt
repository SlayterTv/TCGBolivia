package com.slaytertv.tcgbolivia.ui.adapters.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.databinding.ItemCartabuyBinding
import java.text.SimpleDateFormat

class HomeCardBuyAdapter (
    //creo variable para cuando editen, borren, el item
    val onItemClicked: (Int, CardDosItem) -> Unit,
    //val onEditClicked: (Int, NoteItem) -> Unit,
    //val onDeleteClicked: (Int,NoteItem) -> Unit
) : RecyclerView.Adapter<HomeCardBuyAdapter.MyViewHolder>() {

    private var list: MutableList<CardDosItem> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemCartabuyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }
    fun updateList(list: MutableList<CardDosItem>){
        this.list = list
        notifyDataSetChanged()
    }
    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemChanged(position)
    }
    //cantidad de items
    override fun getItemCount(): Int {
        return list.size
    }
    //ccontinuacion del viewhoklder utilizando los botones dek xml y agregando datos
    inner class MyViewHolder(val binding: ItemCartabuyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CardDosItem){
            binding.buytextName.setText(item.name)
            Glide.with(binding.root).load(item.imageUrl).transform(RoundedCorners(25)).into(binding.Imagebuy)
            binding.buytextSetname.setText(item.set_name)
            binding.buytextSetcode.setText(item.set_code)
            binding.buytextSetraritycode.setText(item.set_rarity)
            binding.buytextSetprice.setText(item.set_price)
            binding.buytextPrecio.setText(item.precio.toString())
            binding.buytextCantidad.setText(item.cantidad.toString())
            binding.buytextCantidad.setOnClickListener { onItemClicked.invoke(adapterPosition,item) }
        }
    }
}