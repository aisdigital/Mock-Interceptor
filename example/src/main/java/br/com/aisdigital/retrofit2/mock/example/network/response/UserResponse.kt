package br.com.aisdigital.retrofit2.mock.example.network.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("displayName")
    val displayName: String?
)
