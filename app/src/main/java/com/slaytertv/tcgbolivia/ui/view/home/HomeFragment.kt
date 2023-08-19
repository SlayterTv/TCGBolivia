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
                toast(item.toString())
                //itemselected(item.id,item.name,"movie", email)
                /*findNavController().navigate(R.id.action_principalFragment_to_chaptersFragment,Bundle().apply {
                    putParcelable("note",item)
                })*/

            }
        )
    }
    val adaptercardsell by lazy {
        HomeCardSellAdapter(
            onItemClicked = { pos, item ->
                toast(item.toString())
                //itemselected(item.id,item.name,"serie", email)
                //val user = MovieItem(item.id,"serie",item.name,item.desc,item.image,item.date,item.season,item.episodes,"","","")
                /*findNavController().navigate(R.id.action_principalFragment_to_chaptersFragment,Bundle().apply {
                    putParcelable("note",item)
                })*/
            }
        )
    }
    val adaptercardtop by lazy {
        HomeCardTopAdapter(
            onItemClicked = { pos, item ->
                toast(item.toString())
                /*itemselected(item.id,"${item.name}","chapter", email)
                findNavController().navigate(R.id.action_principalFragment_to_onlineActivity,Bundle().apply {
                    putString("note",item.link)
                })*/
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
        verificaranonimo()
        observerB()
        observerS()
        observerT()
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

    private fun observerT() {
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
                }
            }
        }
    }

    private fun observerB() {
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
                }
            }
        }
    }

    private fun observerS() {
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
                }
            }
        }
    }

}