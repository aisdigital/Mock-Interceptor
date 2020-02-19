package br.com.aisdigital.retrofit2.mock.example.network.util

import br.com.aisdigital.retrofit2.mock.example.network.ApiResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BaseRetrofitCallback <T: Any>(private val handler: (ApiResult<T>) -> Unit): Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            // body can be null with 204 code
            val body = response.body()
            handler.invoke(ApiResult.Success(body))
        }
        else {
            val error = ApiResult.Error("Error")
            handler.invoke(error)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        val error = ApiResult.Error("Connection Error")
        handler.invoke(error)
    }
}