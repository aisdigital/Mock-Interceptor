package com.aisdigital.retrofit2.mock.example.network.datasource

import com.aisdigital.retrofit2.mock.example.network.ApiResult
import com.aisdigital.retrofit2.mock.example.network.request.LoginRequest
import com.aisdigital.retrofit2.mock.example.network.response.LoginResponse
import com.aisdigital.retrofit2.mock.example.network.response.UserResponse
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(request: LoginRequest): ApiResult<LoginResponse> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoginResponse(user = UserResponse(java.util.UUID.randomUUID().toString(), "Jane Doe"))
            return ApiResult.Success(fakeUser)
        } catch (e: Throwable) {
            return ApiResult.Error("Error logging in",
                IOException(
                    "Error logging in",
                    e
                )
            )
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

