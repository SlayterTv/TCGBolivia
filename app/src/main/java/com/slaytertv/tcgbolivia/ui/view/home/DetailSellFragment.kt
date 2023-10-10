package com.slaytertv.tcgbolivia.ui.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.bumptech.glide.Glide
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.databinding.FragmentDetailSellBinding

class DetailSellFragment : Fragment() {

    val TAG: String = "DetailSellFragment"
    //binding
    lateinit var binding: FragmentDetailSellBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailSellBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recuperardatoscarta()
    }

    private fun recuperardatoscarta() {
        val cardItem = arguments?.getParcelable<CardDosItem>(DetailSellFragment.ARG_CARD_ITEM)

        val setName:TextView = binding.textName
        val setNameTextView: TextView = binding.textSetname
        val setCodeTextView: TextView = binding.textSetcode
        val setRarityTextView: TextView = binding.textSetrarity
        val setPriceTextView: EditText = binding.textPrecio
        val setCantidadTextView: EditText = binding.textCantidad
        Glide.with(this).load(cardItem?.imageUrl).into(binding.imagesell)
        setName.setText(cardItem!!.name)
        setNameTextView.setText(cardItem.set_name)
        setCodeTextView.setText(cardItem.set_code)
        setRarityTextView.setText(cardItem.set_rarity)
        setPriceTextView.setText(cardItem.precio.toString())
        setCantidadTextView.setText(cardItem.cantidad.toString())

    }

    companion object {
        private const val ARG_CARD_ITEM = "arg_card_item"
        fun newInstance(cardItem:CardDosItem): DetailSellFragment {
            val args = Bundle().apply {
                putParcelable(ARG_CARD_ITEM,cardItem)
            }
            val fragment = DetailSellFragment()
            fragment.arguments = args
            return fragment
        }
    }

}