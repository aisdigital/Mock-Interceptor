package com.aisdigital.retrofit2.mock.example.network.request

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("username")
    val user: String,
    @SerializedName("password")
    val password: String
)