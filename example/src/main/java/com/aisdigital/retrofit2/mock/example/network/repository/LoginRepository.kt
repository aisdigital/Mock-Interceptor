package com.aisdigital.retrofit2.mock.example.network.repository

import com.aisdigital.retrofit2.mock.example.network.datasource.LoginDataSource
import com.aisdigital.retrofit2.mock.example.network.ApiResult
import com.aisdigital.retrofit2.mock.example.network.model.User
import com.aisdigital.retrofit2.mock.example.network.request.LoginRequest
import com.aisdigital.retrofit2.mock.example.network.response.LoginResponse

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(request: LoginRequest): ApiResult<LoginResponse> {
        // handle login
        val result = dataSource.login(request)
        onLoginResult(result)
        return result
    }

    private fun onLoginResult(result: ApiResult<LoginResponse>) {
        if (result is ApiResult.Success) {
            result.data.user?.id?.let { id ->
                val displayName = result.data.user.displayName?: "-"
                setLoggedInUser(User(userId = id, displayName = displayName))
            }
        }
    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
