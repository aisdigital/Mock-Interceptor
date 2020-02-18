package com.aisdigital.retrofit2.mock.example.di

import com.aisdigital.retrofit2.mock.example.network.api.AuthApi
import com.aisdigital.retrofit2.mock.example.network.util.RetrofitHelper
import org.koin.dsl.module
import retrofit2.Retrofit

val retrofitModule = module {

    // Retrofit configuration
    single {
        val initClient = RetrofitHelper.initClient()
//        if (BuildConfig.FLAVOR == "mock")
//            initClient.addInterceptor(MockInterceptor(androidContext()))

//        initClient.addInterceptor(PrettyLoggingInterceptor.get())

        RetrofitHelper.initRetrofit(initClient)
    }

    single { (get() as Retrofit).create(AuthApi::class.java) }
}
