package br.com.aisdigital.retrofit2.mock.example.network

class BaseUrl private constructor() {
    companion object {
        const val PATH_DEFAULT = "/api/productio"

        const val PATH_LOGIN = "/v1/login"

        const val PATH_LOGOUT = "/v1/logout"
    }
}