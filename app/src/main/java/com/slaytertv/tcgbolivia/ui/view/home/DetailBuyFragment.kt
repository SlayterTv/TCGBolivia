package com.slaytertv.tcgbolivia.ui.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.fragment.app.viewModels
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.databinding.FragmentDetailBuyBinding
import com.slaytertv.tcgbolivia.ui.viewmodel.home.DetailBuyViewModel
import com.slaytertv.tcgbolivia.util.UiState
import com.slaytertv.tcgbolivia.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailBuyFragment : Fragment() {
    val TAG: String = "DetailBuyFragment"
    //binding
    lateinit var binding: FragmentDetailBuyBinding
    private val detailbuyViewModel: DetailBuyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBuyBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recuperardatoscarta()
        botones()
        observer()
    }

    private fun observer() {
        detailbuyViewModel.registerchat.observe(viewLifecycleOwner){state ->
            when(state){
                is UiState.Loading -> {
                    //progressbar
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Sucess -> {
                    toast(state.data)
                    /*val socialFragment = SocialFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, socialFragment)
                        .commit()*/
                }
            }
        }
    }

    private fun botones() {
        val cardItem = arguments?.getParcelable<CardDosItem>(DetailBuyFragment.ARG_CARD_ITEM)
        binding.chat.setOnClickListener {
            if (cardItem != null) {
                detailbuyViewModel.registerchat(cardItem)
            }
            //revisar if si no da
        }
    }

    private fun recuperardatoscarta() {
        val cardItem = arguments?.getParcelable<CardDosItem>(DetailBuyFragment.ARG_CARD_ITEM)

        val setName: TextView = binding.textName
        val setNameTextView: TextView = binding.textSetname
        val setCodeTextView: TextView = binding.textSetcode
        val setRarityTextView: TextView = binding.textSetrarity
        val setPriceTextView: TextView = binding.textPrecio
        val setCantidadTextView: TextView = binding.textCantidad
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
        fun newInstance(cardItem: CardDosItem): DetailBuyFragment {
            val args = Bundle().apply {
                putParcelable(ARG_CARD_ITEM,cardItem)
            }
            val fragment = DetailBuyFragment()
            fragment.arguments = args
            return fragment
        }
    }
}