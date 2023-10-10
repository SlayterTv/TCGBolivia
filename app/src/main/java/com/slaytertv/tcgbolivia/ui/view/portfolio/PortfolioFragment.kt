package com.slaytertv.tcgbolivia.ui.view.portfolio


import CardsAdapter
import com.slaytertv.tcgbolivia.databinding.FragmentPortfolioBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.slaytertv.tcgbolivia.MainActivity
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.ui.viewmodel.portfolio.PortfolioViewModel
import com.slaytertv.tcgbolivia.util.UiState
import com.slaytertv.tcgbolivia.util.hide
import com.slaytertv.tcgbolivia.util.show
import com.slaytertv.tcgbolivia.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortfolioFragment : Fragment() {
    var catego:String = ""
    private lateinit var binding: FragmentPortfolioBinding
    private val portfolioViewModel: PortfolioViewModel by viewModels()
    val adaptercardsell by lazy {
        CardsAdapter(
            onItemClicked = { pos, item ->
                /*val cardItem = item
                val portfolioVistaFragment = PortfolioVistaFragment.newInstance(cardItem)
                (requireActivity() as MainActivity).switchFragment(portfolioVistaFragment)*/
//                portfolioViewModel.registersell(item)
                navigateToPortfolioVistaFragment(item)
            }
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPortfolioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        botones()
        observer()
        val staggeredGridLayoutManagerS = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.cardsRecyclerView.layoutManager = staggeredGridLayoutManagerS
        binding.cardsRecyclerView.adapter = adaptercardsell
    }
    fun botones(){
        binding.searchButton.setOnClickListener {
            val partialCardName = binding.searchEditText.text.toString()
            portfolioViewModel.searchCards(partialCardName)
        }
        binding.logoyugi.setOnClickListener {
            catego = "yugioh"
            binding.barrabusqueda.show()
            binding.labelinfo.hide()
        }
    }

    private fun observer() {
        portfolioViewModel.cardsLiveData.observe(viewLifecycleOwner) { cards ->
            if (!cards.isNullOrEmpty()) {
                val cardNames = cards.joinToString(", ") { it.name }
                //toast("Cartas encontradas: $cardNames")
            } else {
                toast("No se encontraron cartas")
            }

            // Actualizar los datos en el adaptador
            adaptercardsell.submitList(cards)
            if(cards?.size.toString() == "0"){
                binding.labelinfo.show()
            }else{
                binding.labelinfo.hide()
            }
        }
        portfolioViewModel.registersell.observe(viewLifecycleOwner){ state ->
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

                else -> {}
            }
        }
    }
    private fun navigateToPortfolioVistaFragment(cardItem: CardItem) {
        val args = Bundle().apply {
            putParcelable(ARG_CARD_ITEM, cardItem)
        }
        val portfolioVistaFragment = PortfolioVistaFragment()
        portfolioVistaFragment.arguments = args
        (requireActivity() as MainActivity).switchFragment(portfolioVistaFragment)
    }
    companion object {
        private const val ARG_CARD_ITEM = "arg_card_item"
    }
}
