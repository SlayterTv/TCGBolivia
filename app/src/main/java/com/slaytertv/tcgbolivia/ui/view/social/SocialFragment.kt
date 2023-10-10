package com.slaytertv.tcgbolivia.ui.view.social

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.slaytertv.tcgbolivia.data.model.ChatMessageItem
import com.slaytertv.tcgbolivia.ui.viewmodel.social.SocialChatsViewModel
import com.slaytertv.tcgbolivia.databinding.FragmentSocialBinding
import com.slaytertv.tcgbolivia.ui.adapters.social.SocialChatsAdapter
import com.slaytertv.tcgbolivia.ui.adapters.social.SocialMessageAdapter
import com.slaytertv.tcgbolivia.ui.viewmodel.social.SocialMessageViewModel
import com.slaytertv.tcgbolivia.util.UiState
import com.slaytertv.tcgbolivia.util.hide
import com.slaytertv.tcgbolivia.util.show
import com.slaytertv.tcgbolivia.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialFragment : Fragment() {
    var rutchat :String = ""
    val TAG :String ="SocialFragment"
    //creamos binding
    lateinit var binding: FragmentSocialBinding
    private lateinit var auth: FirebaseAuth
    val viewModel: SocialChatsViewModel by viewModels()
    val viewModelMessage: SocialMessageViewModel by viewModels()
    val adaptersocialchats by lazy {
        SocialChatsAdapter(
            onItemClicked = { pos, item ->
                //toast(item.toString())
                viewModelMessage.getMessages(item.rutachat)
                rutchat = item.rutachat
                binding.barrachat.show()
            }
        )
    }
    val adaptersocialmessage by lazy {
        SocialMessageAdapter(

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
            binding = FragmentSocialBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //llamar observer

        botones()
        verificaranonimo()
        observer()
        listener()
        recyclers()


    }

    private fun botones() {
        binding.chatenviar.setOnClickListener {
            val mensaje:String = binding.chatmessage.text.toString()
            if(mensaje.isNullOrEmpty()){
                toast("agregue un mensaje, dah!")
                return@setOnClickListener
            }
            viewModelMessage.addMessage(rutchat, ChatMessageItem("",mensaje,0))
        }
    }

    private fun recyclers() {
        val staggeredGridLayoutManager = LinearLayoutManager(activity,
            RecyclerView.HORIZONTAL,false)
        binding.recyclerchatcontacts.layoutManager = staggeredGridLayoutManager
        binding.recyclerchatcontacts.adapter = adaptersocialchats

        val staggeredGridLayoutManagerS = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        binding.recyclerchatmessages.layoutManager = staggeredGridLayoutManagerS
        binding.recyclerchatmessages.adapter = adaptersocialmessage

    }

    private fun listener() {
        viewModel.getChats()
    }

    private fun observer() {
        viewModel.getchats.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    //binding.homeProgresbarTop.show()
                }
                is UiState.Failure -> {
                    //binding.homeProgresbarTop.hide()
                    toast(state.error)
                }
                is UiState.Sucess -> {
                    //binding.homeProgresbarTop.hide()
                    adaptersocialchats.updateList(state.data.toMutableList())

                }
            }
        }

        viewModelMessage.getmessages.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    //binding.homeProgresbarTop.show()
                }
                is UiState.Failure -> {
                    //binding.homeProgresbarTop.hide()
                    toast(state.error)
                }
                is UiState.Sucess -> {
                    //binding.homeProgresbarTop.hide()
                    adaptersocialmessage.updateList(state.data.toMutableList())
                }
            }
        }

        viewModelMessage.addmessage.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    //binding.homeProgresbarTop.show()
                }
                is UiState.Failure -> {
                    //binding.homeProgresbarTop.hide()
                    toast(state.error)
                }
                is UiState.Sucess -> {
                    //binding.homeProgresbarTop.hide()
                    //adaptersocialmessage.updateList(state.data.toMutableList())
                    toast(state.data.toString())
                    binding.chatmessage.setText("")
                    adaptersocialmessage.addMessage(state.data)
                }
            }
        }
    }

    private fun verificaranonimo() {
        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null && currentUser.isAnonymous) {
            binding.layoutconectsocial.show()
            binding.layoutchat.hide()
        } else {
            // Usuario autenticado, oculta el botón
            binding.layoutconectsocial.hide()
            binding.layoutchat.show()
        }
    }
}