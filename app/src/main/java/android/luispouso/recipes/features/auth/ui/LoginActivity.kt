package android.luispouso.recipes.features.auth.ui

import android.content.Context
import android.content.Intent
import android.luispouso.recipes.R
import android.luispouso.recipes.core.ui.showToast
import android.luispouso.recipes.core.ui.textWatcherListener
import android.luispouso.recipes.databinding.ActivityLoginBinding
import android.luispouso.recipes.features.auth.vm.LoginViewModel
import android.luispouso.recipes.features.main.ui.MainActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    companion object {
        fun navigate(context: Context) {
            val intent = Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        with(binding) {
            etEmail.textWatcherListener(
                onTextChanged = {
                    viewModel.onEmailChanged(etEmail.text.toString())
                }
            )

            etPassword.textWatcherListener(
                onTextChanged = {
                    viewModel.onPasswordChanged(etPassword.text.toString())
                }
            )

            btnLogin.setOnClickListener {
                viewModel.loginUser(etEmail.text.toString(), etPassword.text.toString())
            }

            btnRegister.setOnClickListener {
                viewModel.registerUser(etEmail.text.toString(), etPassword.text.toString())
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                MainActivity.navigate(this)
            } else {
                showToast(getString(R.string.login_error))
            }
        }

        viewModel.areFieldsValid.observe(this) { areValid ->
            with(binding) {
                btnLogin.isEnabled = areValid
                btnRegister.isEnabled = areValid
            }
        }
    }
}