package com.slaytertv.tcgbolivia

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.slaytertv.tcgbolivia.ui.viewmodel.splash.SplashViewModel
import com.slaytertv.tcgbolivia.ui.viewmodel.splash.SplashViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: SplashViewModelFactory

    private lateinit var viewModel: SplashViewModel

    private val SPLASH_DELAY: Long = 2000 // 2 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)

        GlobalScope.launch {
            delay(SPLASH_DELAY)
            withContext(Dispatchers.Main) {
                checkInternetAndUser()
            }
        }
    }

    private fun checkInternetAndUser() {
        if (!isConnectedToInternet()) {
            showErrorAndExit("No tienes conexión a Internet")
            return
        }

        if (viewModel.isUserLoggedIn()) {
            navigateToMainActivity()
        } else {
            viewModel.signInAnonymously { success, errorMessage ->
                if (success) {
                    navigateToMainActivity()
                } else {
                    showErrorAndExit(errorMessage ?: "Error desconocido")
                }
            }
        }
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun showErrorAndExit(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        // Mostrar mensaje de error y cerrar la app
        finishAffinity()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
