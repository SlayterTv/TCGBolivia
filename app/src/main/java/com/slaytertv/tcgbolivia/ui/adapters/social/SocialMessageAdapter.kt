package com.slaytertv.tcgbolivia.ui.adapters.social

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.slaytertv.tcgbolivia.R
import com.slaytertv.tcgbolivia.data.model.ChatMessageItem
import com.slaytertv.tcgbolivia.databinding.ItemMessageBinding

class SocialMessageAdapter (

) : RecyclerView.Adapter<SocialMessageAdapter.MyViewHolder>() {

    //creo lista mutable para que contenga todos los datos de mi noteitem
    private var list: MutableList<ChatMessageItem> = arrayListOf()
    //viewholder que llama a mi itemnotelayotu.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }
    //posicion de los items
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }
    //muestra los cambios de mi recyclerview
    fun updateList(list: MutableList<ChatMessageItem>){
        this.list = list
        notifyDataSetChanged()
    }

    //remueve items del recyclerview y muestra los cambios
    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemChanged(position)
    }
    fun addMessage(message: ChatMessageItem) {
        list.add(message)
        notifyItemInserted(list.size - 1)
    }
    //cantidad de items
    override fun getItemCount(): Int {
        return list.size
    }
    //ccontinuacion del viewhoklder utilizando los botones dek xml y agregando datos
    inner class MyViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessageItem){
            if(item.senderId == item.isCurrentUser ){
                binding.messageText.setText(item.text)
                binding.messageText.setBackgroundResource(R.drawable.bg_message_user)
            }else{
                binding.messageText.setText(item.text)
                binding.messageText.setBackgroundResource(R.drawable.bg_message_other_user)            }
        }
    }
}