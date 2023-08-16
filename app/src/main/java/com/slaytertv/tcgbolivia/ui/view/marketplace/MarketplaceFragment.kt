package com.slaytertv.tcgbolivia.ui.view.marketplace


import com.slaytertv.tcgbolivia.data.repository.YgoProDeckApiService
import com.slaytertv.tcgbolivia.databinding.FragmentMarketplaceBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class MarketplaceFragment : Fragment() {

    @Inject
    lateinit var ygoProDeckApiService: YgoProDeckApiService

    private lateinit var binding: FragmentMarketplaceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchFirstFourCards()
    }

    private fun fetchFirstFourCards() {
        lifecycleScope.launch {
            try {
                val response = ygoProDeckApiService.getAllCards()
                if (response.isSuccessful) {
                    val cardResponse = response.body()
                    val cards = cardResponse?.data?.take(8) // Tomar las primeras 4 cartas

                    if (!cards.isNullOrEmpty()) {
                        val cardNames = cards.joinToString(", ") { it.name }
                        showToast("Primeras 4 cartas: $cardNames")
                    }
                } else {
                    showToast("Error al obtener las cartas")
                }
            } catch (e: Exception) {
                showToast("Error en la petición")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}