package com.slaytertv.tcgbolivia.ui.adapters.social

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.slaytertv.tcgbolivia.data.model.RutaChatItem
import com.slaytertv.tcgbolivia.databinding.ItemChatsBinding

class SocialChatsAdapter (
    //creo variable para cuando editen, borren, el item
    val onItemClicked: (Int, RutaChatItem) -> Unit,
    //val onEditClicked: (Int, NoteItem) -> Unit,
    //val onDeleteClicked: (Int,NoteItem) -> Unit
) : RecyclerView.Adapter<SocialChatsAdapter.MyViewHolder>() {

    //creo lista mutable para que contenga todos los datos de mi noteitem
    private var list: MutableList<RutaChatItem> = arrayListOf()
    //viewholder que llama a mi itemnotelayotu.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemChatsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }
    //posicion de los items
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }
    //muestra los cambios de mi recyclerview
    fun updateList(list: MutableList<RutaChatItem>){
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
    inner class MyViewHolder(val binding: ItemChatsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RutaChatItem){
            binding.name.setText(item.name)
            Glide.with(binding.root).load(item.image) .transform(
                RoundedCorners(25)
            ).into(binding.imageView)
            binding.linearchats.setOnClickListener { onItemClicked.invoke(adapterPosition,item) }
        }
    }
}