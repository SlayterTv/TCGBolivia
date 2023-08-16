package com.slaytertv.tcgbolivia.ui.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
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
        verificarrecycler()
    }

    private fun verificarrecycler() {
        /*// Supongamos que tienes los adaptadores para tus RecyclerView
        val buyAdapter = // ... (inicializa tu adaptador)
        val sellAdapter = // ... (inicializa tu adaptador)
            binding.homerecyclerviewbuy.adapter = buyAdapter
        binding.homerecyclerviewsell.adapter = sellAdapter

        checkRecyclerViewData(buyAdapter, binding.homerecyclerviewbuy)
        checkRecyclerViewData(sellAdapter, binding.homerecyclerviewsell)*/
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
    //revisar si tienes datos los recycler
    private fun checkRecyclerViewData(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        if (adapter.itemCount > 0) {
            binding.homesinanda.hide()
            binding.homeconcosas.show()
        } else {
            binding.homesinanda.show()
            binding.homeconcosas.hide()
        }
    }
}