package com.slaytertv.tcgbolivia.ui.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.slaytertv.tcgbolivia.R
import com.slaytertv.tcgbolivia.databinding.FragmentProfileBinding
import com.slaytertv.tcgbolivia.ui.view.profile.tabprofile.SettingsFragment
import com.slaytertv.tcgbolivia.ui.view.profile.tabprofile.StatsFragment
import com.slaytertv.tcgbolivia.ui.view.profile.tabprofile.SupportFragment
import com.slaytertv.tcgbolivia.ui.viewmodel.profile.tabprofile.ViewpagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    val TAG :String ="ProfileFragment"
    //creamos binding
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (this::binding.isInitialized){
            return binding.root
        }else {
            binding = FragmentProfileBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        // Crear el adaptador
        val adapter = ViewpagerAdapter(requireActivity())

        // Agregar fragmentos al adaptador
        adapter.addFragment(StatsFragment(), "Stats")
        adapter.addFragment(SettingsFragment(), "Settings")
        adapter.addFragment(SupportFragment(), "Support")

        // Configurar el ViewPager2 con el adaptador
        binding.viewPager.adapter = adapter

        // Configurar el TabLayout con el ViewPager2
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = adapter.fragments[position].second
        }.attach()
    }
}
