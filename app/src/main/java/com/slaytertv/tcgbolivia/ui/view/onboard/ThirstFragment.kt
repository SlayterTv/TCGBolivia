package com.slaytertv.tcgbolivia.ui.view.onboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.slaytertv.tcgbolivia.MainActivity
import com.slaytertv.tcgbolivia.R
import com.slaytertv.tcgbolivia.SliderActivity
import com.slaytertv.tcgbolivia.SplashActivity
import com.slaytertv.tcgbolivia.databinding.FragmentThirstBinding
import com.slaytertv.tcgbolivia.util.SharedPrefConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirstFragment : Fragment() {
    lateinit var binding: FragmentThirstBinding
    private val authLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mainIntent = Intent(requireContext(), SliderActivity::class.java)
            startActivity(mainIntent)
            requireActivity().finish()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentThirstBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragslid2 .setOnClickListener {
            findNavController().navigate(R.id.action_thirstFragment_to_SecondFragment)
        }
        binding.activitauth.setOnClickListener {

            val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val isFirstRun = sharedPreferences.getBoolean(SharedPrefConstants.SLIDER_INICIAL, true)
            if (isFirstRun) {
                sharedPreferences.edit().putBoolean(SharedPrefConstants.SLIDER_INICIAL, false).apply()
            }
            val authIntent = Intent(requireContext(), SplashActivity::class.java)
            authLauncher.launch(authIntent)
        }
    }
}