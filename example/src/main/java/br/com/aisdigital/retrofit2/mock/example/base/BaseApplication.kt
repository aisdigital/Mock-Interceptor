package br.com.aisdigital.retrofit2.mock.example.base

import android.app.Application
import br.com.aisdigital.retrofit2.mock.example.di.repositoryModule
import br.com.aisdigital.retrofit2.mock.example.di.retrofitModule
import br.com.aisdigital.retrofit2.mock.example.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    retrofitModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}