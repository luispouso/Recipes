package android.luispouso.recipes.features.splash.ui

import android.annotation.SuppressLint
import android.luispouso.recipes.features.auth.ui.LoginActivity
import android.luispouso.recipes.features.main.ui.MainActivity
import android.luispouso.recipes.features.splash.vm.SplashViewModel
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.navigationEvent.observe(this) { destination ->
            when (destination) {
                SplashViewModel.NavigationDestination.Main -> MainActivity.navigate(this)
                SplashViewModel.NavigationDestination.Login -> LoginActivity.navigate(this)
            }
        }
    }
}