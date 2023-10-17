package com.slaytertv.tcgbolivia.ui.view.marketplace



import com.slaytertv.tcgbolivia.databinding.FragmentMarketplaceBinding
import com.slaytertv.tcgbolivia.ui.viewmodel.marketplace.MarketplaceViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.slaytertv.tcgbolivia.ui.adapters.marketplace.MarketplaceCardBuyAdapter
import com.slaytertv.tcgbolivia.util.UiState
import com.slaytertv.tcgbolivia.util.hide
import com.slaytertv.tcgbolivia.util.show
import com.slaytertv.tcgbolivia.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketplaceFragment : Fragment() {
    var catego:String =""

    private lateinit var binding: FragmentMarketplaceBinding
    private val marketplaceViewModel: MarketplaceViewModel by viewModels()
    val marketpalceadaptercardbuy by lazy {
        MarketplaceCardBuyAdapter(
            onItemClicked = { pos, item ->
                //

                marketplaceViewModel.registerbuy(item)
            }
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        botones()
        observer()
        val staggeredGridLayoutManagerS = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.cardsbuyRecyclerView.layoutManager = staggeredGridLayoutManagerS
        binding.cardsbuyRecyclerView.adapter = marketpalceadaptercardbuy
    }
    fun botones(){
        binding.searchcardbuyButton.setOnClickListener {
            val partialCardName = binding.searchcardbuyEditText.text.toString()
            marketplaceViewModel.searchCards(partialCardName,catego)
        }
        binding.logoyugi.setOnClickListener {
            catego = "yugioh"
            binding.barrabusqueda.show()
            binding.layouttextinfo.hide()
        }
    }

    private fun observer() {
        marketplaceViewModel.cardsLiveData.observe(viewLifecycleOwner) { cards ->
            if (!cards.isNullOrEmpty()) {
                val cardNames = cards.joinToString(", ") { it.name }
                //toast("Cartas encontradas: $cardNames")
            } else {
                toast("No se encontraron cartas")
            }

            // Actualizar los datos en el adaptador
            marketpalceadaptercardbuy.submitList(cards)
            if(cards?.size.toString() == "0"){
                binding.layouttextinfo.show()
            }else{
                binding.layouttextinfo.hide()
            }
        }
        marketplaceViewModel.registerbuy.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    //progressbar
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Sucess -> {
                    toast(state.data)

                }
            }
        }
    }
}
