package com.slaytertv.tcgbolivia.ui.adapters.portfolio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.slaytertv.tcgbolivia.data.model.CardSet
class CardSetSpinnerAdapter(context: Context, cardSets: List<CardSet>) :
    ArrayAdapter<CardSet>(context, 0, cardSets) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val cardSet = getItem(position)
        val view = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = cardSet?.set_name
        return view
    }
}