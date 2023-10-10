package com.slaytertv.tcgbolivia.ui.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.slaytertv.tcgbolivia.MainActivity
import com.slaytertv.tcgbolivia.databinding.FragmentHomeBinding
import com.slaytertv.tcgbolivia.ui.view.marketplace.MarketplaceFragment
import com.slaytertv.tcgbolivia.ui.view.portfolio.PortfolioFragment
import com.slaytertv.tcgbolivia.util.hide
import com.slaytertv.tcgbolivia.util.show
import dagger.hilt.android.AndroidEntryPoint
import com.slaytertv.tcgbolivia.AuthActivity
import com.slaytertv.tcgbolivia.data.model.CardDosItem
import com.slaytertv.tcgbolivia.ui.adapters.home.HomeCardBuyAdapter
import com.slaytertv.tcgbolivia.ui.adapters.home.HomeCardSellAdapter
import com.slaytertv.tcgbolivia.ui.adapters.home.HomeCardTopAdapter
import com.slaytertv.tcgbolivia.ui.viewmodel.home.HomeCardsBuyViewModel
import com.slaytertv.tcgbolivia.ui.viewmodel.home.HomeCardsSellViewModel
import com.slaytertv.tcgbolivia.ui.viewmodel.home.HomeCardsTopViewModel
import com.slaytertv.tcgbolivia.util.UiState
import com.slaytertv.tcgbolivia.util.toast


@AndroidEntryPoint
class HomeFragment : Fragment() {
    val TAG :String ="HomeFragment"
    //creamos binding
    lateinit var binding: FragmentHomeBinding
    //
    private lateinit var auth: FirebaseAuth
    //
    private val authLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mainIntent = Intent(requireContext(), MainActivity::class.java)
            startActivity(mainIntent)
            requireActivity().finish()
        }
    }
    //viewmodels
    val viewModelCardbuy: HomeCardsBuyViewModel by viewModels()//cardbuy
    val viewModelCardsell: HomeCardsSellViewModel by viewModels()//cardsell
    val viewModelCardTop: HomeCardsTopViewModel by viewModels()//cardtop
    //adapter que mandara los items al adapter para poder realizar las funciones que indica
    val adaptercardbuy by lazy {
        HomeCardBuyAdapter(
            onItemClicked = { pos, item ->
                //
                //toast(item.toString())
                navigateToDetailSellorBuyFragment(item,"buy")
            }
        )
    }
    val adaptercardsell by lazy {
        HomeCardSellAdapter(
            onItemClicked = { pos, item ->
                navigateToDetailSellorBuyFragment(item,"sell")
            }
        )
    }
    val adaptercardtop by lazy {
        HomeCardTopAdapter(
            onItemClicked = { pos, item ->
                toast(item.toString())
            }
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (this::binding.isInitialized){
            return binding.root
        }else {
            binding = FragmentHomeBinding.inflate(layoutInflater)
            return binding.root
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //llamar observer
        botones()
        observer()
        //
        viewModelCardbuy.getCardBuy()
        viewModelCardsell.getCardSell()
        viewModelCardTop.getCardTop()
        //recycleradd
        val staggeredGridLayoutManagerS = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        val staggeredGridLayoutManagerT = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
        val staggeredGridLayoutManagerB = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.homerecyclerviewbuy.layoutManager = staggeredGridLayoutManagerB
        binding.homerecyclerviewsell.layoutManager = staggeredGridLayoutManagerS
        binding.homerecyclerviewTop.layoutManager = staggeredGridLayoutManagerT

        binding.homerecyclerviewbuy.adapter = adaptercardbuy
        binding.homerecyclerviewsell.adapter = adaptercardsell
        binding.homerecyclerviewTop.adapter = adaptercardtop
    }
    private fun mostrarocultartexts(a:String,b:String) {
        if(a == "top" && b == "0" ){
            binding.homerecyclerviewTop.hide()
            binding.texttop.hide()
            binding.homesinanda.show()
        }
        else{
            binding.homerecyclerviewTop.show()
            binding.texttop.show()
        }
        if(a == "buy" && b == "0"){
            binding.textbuy.hide()
            binding.homerecyclerviewbuy.hide()
            binding.homesinanda.show()
        }else{
            binding.homesinanda.hide()
            binding.textbuy.show()
            binding.homerecyclerviewbuy.show()
        }
        if(a == "sell" && b == "0"){
            binding.textsell.hide()
            binding.homerecyclerviewsell.hide()
            binding.homesinanda.show()
        }
        else{
            binding.textsell.show()
            binding.homerecyclerviewsell.show()
            binding.homesinanda.hide()
        }
        verificaranonimo()
    }
    private fun verificaranonimo() {
        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null && currentUser.isAnonymous) {
            binding.homebuttonlogin.show()
            binding.textView8.show()
        } else {
            // Usuario autenticado, oculta el botón
            binding.homebuttonlogin.hide()
            binding.textView8.hide()
        }
    }
    fun botones(){
        binding.buttonsell.setOnClickListener {
            (requireActivity() as MainActivity).switchFragment(PortfolioFragment())
        }
        binding.buttonbuy.setOnClickListener {
            (requireActivity() as MainActivity).switchFragment(MarketplaceFragment())
        }
        binding.homebuttonlogin.setOnClickListener {
            val authIntent = Intent(requireContext(), AuthActivity::class.java)
            authLauncher.launch(authIntent)
        }
    }
    private fun observer() {
        viewModelCardTop.cardtop.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.homeProgresbarTop.show()
                }
                is UiState.Failure -> {
                    binding.homeProgresbarTop.hide()
                    toast(state.error)
                }
                is UiState.Sucess -> {
                    binding.homeProgresbarTop.hide()
                    adaptercardtop.updateList(state.data.toMutableList())
                    mostrarocultartexts("top",state.data.toMutableList().size.toString())
                }
            }
        }
        viewModelCardbuy.cardbuy.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.homeProgresbarbuy.show()
                }
                is UiState.Failure -> {
                    binding.homeProgresbarbuy.hide()
                    toast(state.error)
                }
                is UiState.Sucess -> {
                    binding.homeProgresbarbuy.hide()
                    adaptercardbuy.updateList(state.data.toMutableList())
                    mostrarocultartexts("buy",state.data.toMutableList().size.toString())
                }
            }
        }
        viewModelCardsell.cardsell.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.homeProgressbarsell.show()
                }
                is UiState.Failure -> {
                    binding.homeProgressbarsell.hide()
                    toast(state.error)
                }
                is UiState.Sucess -> {
                    binding.homeProgressbarsell.hide()
                    adaptercardsell.updateList(state.data.toMutableList())
                    mostrarocultartexts("sell",state.data.toMutableList().size.toString())
                }
            }
        }
    }
    private fun navigateToDetailSellorBuyFragment(cardDosItem:CardDosItem,buyorsell:String){
        val args = Bundle().apply {
            putParcelable(ARG_CARD_ITEM, cardDosItem)
        }
        when(buyorsell){
            "buy" -> {
                val detailBuyFragment = DetailBuyFragment()
                detailBuyFragment.arguments = args
                (requireActivity() as MainActivity).switchFragment(detailBuyFragment)
            }
            "sell" -> {
                val detailSellFragment = DetailSellFragment()
                detailSellFragment.arguments = args
                (requireActivity() as MainActivity).switchFragment(detailSellFragment)
            }
        }
    }
    companion object {
        private const val ARG_CARD_ITEM = "arg_card_item"
    }
}