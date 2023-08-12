package com.slaytertv.tcgbolivia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.slaytertv.tcgbolivia.databinding.ActivityMainBinding
import com.slaytertv.tcgbolivia.ui.view.home.HomeFragment
import com.slaytertv.tcgbolivia.ui.view.marketplace.MarketplaceFragment
import com.slaytertv.tcgbolivia.ui.view.portfolio.PortfolioFragment
import com.slaytertv.tcgbolivia.ui.view.profile.ProfileFragment
import com.slaytertv.tcgbolivia.ui.view.social.SocialFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Configurar la navegación con BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navigation_marketplace -> {
                    replaceFragment(MarketplaceFragment())
                    true
                }
                R.id.navigation_social -> {
                    replaceFragment(SocialFragment())
                    true
                }
                R.id.navigation_portfolio -> {
                    replaceFragment(PortfolioFragment())
                    true
                }
                R.id.navigation_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        // Establecer el fragmento por defecto al abrir la actividad
        binding.bottomNavigationView.selectedItemId = R.id.navigation_home

    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}