package com.slaytertv.tcgbolivia.ui.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.slaytertv.tcgbolivia.R
import com.slaytertv.tcgbolivia.databinding.FragmentForgotBinding
import com.slaytertv.tcgbolivia.ui.viewmodel.auth.ForgotPassowrdViewModel
import com.slaytertv.tcgbolivia.util.UiState
import com.slaytertv.tcgbolivia.util.clickdisable
import com.slaytertv.tcgbolivia.util.clickenable
import com.slaytertv.tcgbolivia.util.hide
import com.slaytertv.tcgbolivia.util.show
import com.slaytertv.tcgbolivia.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotFragment : Fragment() {
    val TAG :String ="ForgotPasswordFragment"
    //creamos binding
    lateinit var binding: FragmentForgotBinding
    //viewmodel para usar authviewmodel
    val viewModel: ForgotPassowrdViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (this::binding.isInitialized){
            return binding.root
        }else {
            binding = FragmentForgotBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //llamar observer
        initListeners()
        observer()
    }
    private fun initListeners() {
        with(binding){
            forgotPassBtn.setOnClickListener {
                viewModel.forgotPassword(emailEt.text.toString())
            }
        }
    }

    private fun observer() {
        viewModel.forgotPassword.observe(viewLifecycleOwner){state ->
            when(state){
                is UiState.Loading -> {
                    handleLoading(isLoading = true)
                }
                is UiState.Failure -> {
                    handleLoading(isLoading = false)
                    toast(state.error)
                }
                is UiState.Sucess -> {
                    handleLoading(isLoading = false)
                    toast(state.data)
                    findNavController().navigate(R.id.action_forgotFragment_to_loginFragment)
                }
            }
        }
    }
    //cambia el esto del boton y el progressbar
    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                binding.forgotPassBtn.setText("")
                binding.forgotPassBtn.clickdisable()
                binding.forgotPassProgress.show()
            } else {
                binding.forgotPassBtn.setText("Send")
                binding.forgotPassBtn.clickenable()
                binding.forgotPassProgress.hide()
            }
        }
    }


}