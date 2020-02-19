package br.com.aisdigital.retrofit2.mock.example.ui.login.view

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import br.com.aisdigital.retrofit2.mock.example.R
import br.com.aisdigital.retrofit2.mock.example.network.ApiResult
import br.com.aisdigital.retrofit2.mock.example.network.response.LoginResponse
import br.com.aisdigital.retrofit2.mock.example.ui.home.view.HomeActivity
import br.com.aisdigital.retrofit2.mock.example.ui.login.viewmodel.LoginViewModel
import br.com.aisdigital.retrofit2.mock.example.databinding.ActivityLoginBinding
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : br.com.aisdigital.retrofit2.mock.example.base.BaseActivity() {

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    val viewModel by viewModel<LoginViewModel>()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initObserver()
        initEdittextChanges()
    }

    private fun initObserver() {
        viewModel.loginFormState.observe(this, Observer {
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

        viewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            hideLoading()
            if (loginResult is ApiResult.Success && loginResult.data != null) {
                updateUiWithUser(loginResult.data)
            } else {
                showLoginFailed(getString(R.string.login_failed))
            }
        })
    }

    private fun initEdittextChanges() {
        binding.username.afterTextChanged {
            viewModel.loginDataChanged(
                binding.username.text.toString(),
                binding.password.text.toString()
            )
        }

        binding.password.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            }

            binding.login.setOnClickListener {
                requestLogin()
            }
        }
    }

    private fun updateUiWithUser(response: LoginResponse) {
        val welcome = getString(R.string.welcome)
        val displayName = response.user?.displayName ?: "-"

        showSnackBarMessage(binding.root, "$welcome $displayName", onDismissed = {
            HomeActivity.startActivity(this)
        })
    }

    private fun requestLogin() {
        showLoading()
        viewModel.login(
            binding.username.text.toString(),
            binding.password.text.toString()
        )
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
        showSnackBarMessage(binding.root, errorString, onRetry = {
            requestLogin()
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
