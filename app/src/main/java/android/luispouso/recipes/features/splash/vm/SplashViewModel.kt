package android.luispouso.recipes.features.splash.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    sealed class NavigationDestination {
        object Main : NavigationDestination()
        object Login : NavigationDestination()
    }

    private val _navigationEvent = MutableLiveData<NavigationDestination>()
    val navigationEvent: LiveData<NavigationDestination> = _navigationEvent

    init {
        handleNavigation()
    }

    private fun handleNavigation() {
        _navigationEvent.value = if (auth.currentUser != null) {
            NavigationDestination.Main
        } else {
            NavigationDestination.Login
        }
    }
}