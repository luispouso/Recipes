package android.luispouso.recipes.features.auth.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _areFieldsValid = MutableLiveData<Boolean>()
    val areFieldsValid: LiveData<Boolean> = _areFieldsValid

    private var email: String = ""
    private var password: String = ""

    fun onEmailChanged(newEmail: String) {
        email = newEmail
        validateFields()
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword
        validateFields()
    }

    private fun validateFields() {
        _areFieldsValid.value = email.isNotBlank() && password.isNotBlank() && password.length > 5
    }

    fun loginUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loginSuccess.value = task.isSuccessful
            }
    }

    fun registerUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loginSuccess.value = task.isSuccessful
            }
    }
}