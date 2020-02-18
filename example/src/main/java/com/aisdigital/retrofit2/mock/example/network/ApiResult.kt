package com.aisdigital.retrofit2.mock.example.network

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ApiResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val prettyString: String, val exception: Exception) : ApiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$prettyString]"
        }
    }
}
