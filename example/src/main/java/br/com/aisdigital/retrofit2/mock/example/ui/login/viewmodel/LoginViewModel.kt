package br.com.aisdigital.retrofit2.mock.example.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import br.com.aisdigital.retrofit2.mock.example.network.repository.LoginRepository
import br.com.aisdigital.retrofit2.mock.example.network.ApiResult
import br.com.aisdigital.retrofit2.mock.example.R
import br.com.aisdigital.retrofit2.mock.example.network.request.LoginRequest
import br.com.aisdigital.retrofit2.mock.example.network.response.LoginResponse
import br.com.aisdigital.retrofit2.mock.example.ui.login.view.LoginFormState

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    val _loginResult = MutableLiveData<ApiResult<LoginResponse>>()
    val loginResult: LiveData<ApiResult<LoginResponse>> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        loginRepository.login(LoginRequest(username, password)) {
            _loginResult.value = it
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value =
                LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value =
                LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value =
                LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
