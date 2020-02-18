package br.com.aisdigital.retrofit2.mock.example.di

import br.com.aisdigital.retrofit2.mock.MockInterceptor
import br.com.aisdigital.retrofit2.mock.example.BuildConfig
import br.com.aisdigital.retrofit2.mock.example.network.api.AuthApi
import br.com.aisdigital.retrofit2.mock.example.network.util.RetrofitHelper
import br.com.aisdigital.retrofit2.util.PrettyLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

val retrofitModule = module {

    // Retrofit configuration
    single {
        val initClient = RetrofitHelper.initClient()
        if (BuildConfig.FLAVOR == "mock")
            initClient.addInterceptor(MockInterceptor(androidContext()))

        initClient.addInterceptor(PrettyLoggingInterceptor.getInterceptor())

        RetrofitHelper.initRetrofit(initClient)
    }

    single { (get() as Retrofit).create(AuthApi::class.java) }
}
