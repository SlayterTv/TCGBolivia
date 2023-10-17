package com.slaytertv.tcgbolivia.ui.view.portfolio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.slaytertv.tcgbolivia.R
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.data.model.CardItem
import com.slaytertv.tcgbolivia.databinding.FragmentPortfolioVistaBinding
import com.slaytertv.tcgbolivia.ui.adapters.portfolio.CardImagesAdapter
import com.slaytertv.tcgbolivia.ui.adapters.portfolio.CardSetSpinnerAdapter
import com.slaytertv.tcgbolivia.ui.viewmodel.portfolio.PortfolioViewModel
import com.slaytertv.tcgbolivia.util.UiState
import com.slaytertv.tcgbolivia.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortfolioVistaFragment : Fragment() {
    private lateinit var binding: FragmentPortfolioVistaBinding
    private val portfoliovistaViewModel:PortfolioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPortfolioVistaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recuperardatoscarta()
        botones()
        observer()

    }

    private fun observer() {
        portfoliovistaViewModel.registersell.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {

                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Sucess -> {
                    toast(state.data)
                    // Al finalizar las acciones, volver al HomeFragment
                    val portfolioFragment = PortfolioFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, portfolioFragment)
                        .commit()
                }
            }

        }
    }

    private fun botones() {
        binding.savesellcard.setOnClickListener {
            val cantidadText = binding.cardCantidadEditText.text.toString()
            val precioText = binding.cardPrecioEditText.text.toString()
            val imagenText = binding.selectedImageTextView.text.toString()
            if(imagenText.isNullOrEmpty() ){
                Toast.makeText(context, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(precioText.isNullOrEmpty() ){
                Toast.makeText(context, "Agregue Precio de Venta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(cantidadText.isNullOrEmpty()){
                Toast.makeText(context, "Agregue una Cantidad de cartas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cardItem = arguments?.getParcelable<CardItem>(ARG_CARD_ITEM)
            val notes = CardDosItem(
                id = cardItem!!.id,
                idcard = cardItem.id.toString(),
                name = cardItem.name,
                type = cardItem.type,
                frameType = cardItem.frameType,
                desc = cardItem.desc,
                atk = cardItem.atk,
                def = cardItem.def,
                level = cardItem.level,
                race = cardItem.race,
                attribute = cardItem.attribute,
                imageUrl = binding.selectedImageTextView.text.toString(),
                juego = cardItem.juego ,
                precio = binding.cardPrecioEditText.text.toString().toInt(),
                cantidad = binding.cardCantidadEditText.text.toString().toInt(),
                user = "",
                useridcard = "",
                set_name=binding.setNameTextView.text.toString(),
                set_code=binding.setCodeTextView.text.toString(),
                set_rarity=binding.setRarityTextView.text.toString(),
                set_rarity_code="",
                set_price=binding.setPriceTextView.text.toString()
            )
            //Log.e("carditem",cardItem.toString())
            //Log.e("carddositem",notes.toString())
            portfoliovistaViewModel.registersell(notes)


        }
    }

    private fun recuperardatoscarta() {
//recuepramos datos de portfoliofragment
        val cardItem = arguments?.getParcelable<CardItem>(ARG_CARD_ITEM)
        //colocamos a text el carditem
        binding.portfolioviewname.text = cardItem?.name
        //emptylist para los sets y las imagenes
        val cardSets = cardItem!!.card_sets ?: emptyList()
        val cardImages = cardItem.card_images ?: emptyList()
        //sacamos los botones para usar
        val cardSetSpinner: Spinner = binding.cardSetSpinner
        val setCodeTextView: TextView = binding.setCodeTextView
        val setRarityTextView: TextView = binding.setRarityTextView
        val setPriceTextView: TextView = binding.setPriceTextView
        val setNameTextView: TextView = binding.setNameTextView
        //agregamos el adapter al spinner para q muestre los sets
        val adapter = CardSetSpinnerAdapter(requireContext(), cardSets)
        cardSetSpinner.adapter = adapter
        //si se selecciona un elemento del spinner nos cambia los datos de los otros 4 text
        cardSetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCardSet = cardSets[position]
                setCodeTextView.text = selectedCardSet.set_code
                setRarityTextView.text = selectedCardSet.set_rarity
                setPriceTextView.text = selectedCardSet.set_price
                setNameTextView.text = selectedCardSet.set_name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se hace nada si no hay selección
            }
        }

        // Configurar RecyclerView y su adaptador para las imagenes
        val adapterima = CardImagesAdapter(cardImages)
        //indicamos q sea horizontal
        val staggeredGridLayoutManagerT = LinearLayoutManager(activity,
            RecyclerView.HORIZONTAL,false)
        binding.cardImagesRecyclerView.layoutManager = staggeredGridLayoutManagerT
        binding.cardImagesRecyclerView.adapter = adapterima
        // Configurar listener para la selección de imagen
        adapterima.setOnImageClickListener { imageUrl ->
            binding.selectedImageTextView.text = imageUrl
        }
    }


    companion object {
        private const val ARG_CARD_ITEM = "arg_card_item"

        fun newInstance(cardItem: CardItem): PortfolioVistaFragment {
            val args = Bundle().apply {
                putParcelable(ARG_CARD_ITEM, cardItem)
            }
            val fragment = PortfolioVistaFragment()
            fragment.arguments = args
            return fragment
        }
    }

}