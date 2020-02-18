package com.aisdigital.retrofit2.mock.example.network.util

import com.aisdigital.retrofit2.mock.example.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHelper private constructor() {

    companion object {
        private const val TIMEOUT: Long = 15 // seconds

        fun initRetrofit(clientBuilder: OkHttpClient.Builder): Retrofit {
            val httpClient = clientBuilder.build()
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory
                        .create(
                            GsonBuilder()
                                .serializeNulls()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                .create()
                        )
                )
                .client(httpClient)
                .build()
        }

        fun initClient(): OkHttpClient.Builder {
            return initClient(Dispatcher())
        }

        /**
         * Dispatcher is used to use async or sync threads.
         */
        fun initClient(dispatcher: Dispatcher): OkHttpClient.Builder {
            val httpClient = OkHttpClient.Builder()
            httpClient.dispatcher(dispatcher)
            httpClient.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            httpClient.readTimeout(TIMEOUT, TimeUnit.SECONDS)
            httpClient.writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            return httpClient
        }

    }
}