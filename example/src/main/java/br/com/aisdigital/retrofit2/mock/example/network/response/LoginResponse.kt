package br.com.aisdigital.retrofit2.mock.example.network.response

import com.google.gson.annotations.SerializedName

/**
 * Authentication result : success (user details)
 */
data class LoginResponse(
    @SerializedName("user")
    val user: UserResponse?
)
