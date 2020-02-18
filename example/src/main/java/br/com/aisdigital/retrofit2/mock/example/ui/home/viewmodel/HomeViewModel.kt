package br.com.aisdigital.retrofit2.mock.example.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.aisdigital.retrofit2.mock.example.network.ApiResult
import br.com.aisdigital.retrofit2.mock.example.network.repository.LoginRepository

class HomeViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _logoutResult = MutableLiveData<ApiResult<Void>>()
    val logoutResult: LiveData<ApiResult<Void>> = _logoutResult

    fun logout() {
        loginRepository.logout {
            _logoutResult.value = it
        }
    }
}
