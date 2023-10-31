package com.slaytertv.tcgbolivia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
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

        //inicializar admob
        MobileAds.initialize(this)
        initLoadAds()




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
    private fun initLoadAds(){
        val adRequest : AdRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object  : AdListener(){
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }


        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
    fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}