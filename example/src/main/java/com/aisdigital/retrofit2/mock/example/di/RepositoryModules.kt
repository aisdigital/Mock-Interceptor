package com.aisdigital.retrofit2.mock.example.di

import com.aisdigital.retrofit2.mock.example.network.datasource.LoginDataSource
import com.aisdigital.retrofit2.mock.example.network.repository.LoginRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LoginRepository> {
        LoginRepository(
            LoginDataSource()
        )
    }
}
