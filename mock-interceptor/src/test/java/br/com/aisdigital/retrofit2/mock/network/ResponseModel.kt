package br.com.aisdigital.retrofit2.mock.network

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int
)