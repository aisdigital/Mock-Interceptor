package br.com.aisdigital.retrofit2.mock.network

import com.google.gson.annotations.SerializedName

data class RequestModel(
    @SerializedName("email") val email: String
)
