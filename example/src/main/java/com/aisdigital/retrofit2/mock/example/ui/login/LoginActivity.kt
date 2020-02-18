package com.aisdigital.retrofit2.mock.example.ui.login

import android.app.Activity
import androidx.lifecycle.Observer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.aisdigital.retrofit2.mock.example.R
import com.aisdigital.retrofit2.mock.example.base.BaseActivity
import com.aisdigital.retrofit2.mock.example.databinding.ActivityLoginBinding
import com.aisdigital.retrofit2.mock.example.network.ApiResult
import com.aisdigital.retrofit2.mock.example.network.response.LoginResponse
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {

    private val loginViewModel by viewModel<LoginViewModel>()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            binding.login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                binding.username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                binding.password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            hideLoading()
            if (loginResult is ApiResult.Success) {
                updateUiWithUser(loginResult.data)
            } else {
                showLoginFailed(getString(R.string.login_failed))
            }
        })

        binding.username.afterTextChanged {
            loginViewModel.loginDataChanged(
                binding.username.text.toString(),
                binding.password.text.toString()
            )
        }

        binding.password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            }

            binding.login.setOnClickListener {
                onClickLogin()
            }
        }
    }

    private fun updateUiWithUser(response: LoginResponse) {
        val welcome = getString(R.string.welcome)
        val displayName = response.user?.displayName ?: "-"
        // TODO : initiate successful logged in experience

        showSnackBarMessage(binding.root, "$welcome $displayName", onDismissed = {
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

    private fun onClickLogin() {
        showLoading()
        loginViewModel.login(
            binding.username.text.toString(),
            binding.password.text.toString()
        )
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
        showSnackBarMessage(binding.root, errorString, onRetry = {
            onClickLogin()
        })
    }

    override fun hideLoading() {
        binding.loading.visibility = View.GONE
    }

    override fun showLoading() {
        binding.loading.visibility = View.VISIBLE
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
