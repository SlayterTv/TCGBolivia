package com.slaytertv.tcgbolivia.ui.view.social

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.slaytertv.tcgbolivia.R
import com.slaytertv.tcgbolivia.databinding.FragmentSocialBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialFragment : Fragment() {
    val TAG :String ="SocialFragment"
    //creamos binding
    lateinit var binding: FragmentSocialBinding
    //viewmodel para usar authviewmodel
    //val viewModel: ForgotPassowrdViewModel by viewModels()

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

    }
}