package com.aisdigital.retrofit2.mock.example.network.datasource

import com.aisdigital.retrofit2.mock.example.network.ApiResult
import com.aisdigital.retrofit2.mock.example.network.request.LoginRequest
import com.aisdigital.retrofit2.mock.example.network.response.LoginResponse
import com.aisdigital.retrofit2.mock.example.network.response.UserResponse

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(request: LoginRequest, handler: (ApiResult<LoginResponse>) -> Unit){
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoginResponse(user = UserResponse(java.util.UUID.randomUUID().toString(), "Jane Doe"))
            handler.invoke(ApiResult.Success(fakeUser))
        } catch (e: Throwable) {
            handler.invoke(ApiResult.Error("Error logging in", e))
        }
    }

    fun logout(handler: (ApiResult<Void>) -> Unit) {

        handler.invoke(ApiResult.Success())
    }
}

