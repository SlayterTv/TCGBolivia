package com.slaytertv.tcgbolivia.ui.view.portfolio


import CardsAdapter
import com.slaytertv.tcgbolivia.databinding.FragmentPortfolioBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.slaytertv.tcgbolivia.ui.viewmodel.portfolio.PortfolioViewModel
import com.slaytertv.tcgbolivia.util.UiState
import com.slaytertv.tcgbolivia.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortfolioFragment : Fragment() {

    private val portfolioViewModel: PortfolioViewModel by viewModels()
    private lateinit var binding: FragmentPortfolioBinding
    //private lateinit var cardsAdapter: CardsAdapter

    val adaptercardbuy by lazy {
        CardsAdapter(
            onItemClicked = { pos, item ->
                portfolioViewModel.registerbuy(item)
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
        binding.cardsRecyclerView.adapter = adaptercardbuy

        /*val layoutManager = GridLayoutManager(requireContext(), 2) // 2 columnas
        binding.cardsRecyclerView.layoutManager = layoutManager
        cardsAdapter = CardsAdapter()
        binding.cardsRecyclerView.adapter = cardsAdapter*/

    }
    fun botones(){
        binding.searchButton.setOnClickListener {
            val partialCardName = binding.searchEditText.text.toString()
            portfolioViewModel.searchCards(partialCardName)
        }
    }

    private fun observer() {
        portfolioViewModel.cardImageUrls.observe(viewLifecycleOwner) { imageUrls ->
            adaptercardbuy.setImageUrls(imageUrls)
        }
        portfolioViewModel.cardsLiveData.observe(viewLifecycleOwner) { cards ->
            if (!cards.isNullOrEmpty()) {
                val cardNames = cards.joinToString(", ") { it.name }
                //toast("Cartas encontradas: $cardNames")
            } else {
                toast("No se encontraron cartas")
            }

            // Actualizar los datos en el adaptador
            adaptercardbuy.submitList(cards)
        }
        portfolioViewModel.registerbuy.observe(viewLifecycleOwner){state ->
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
