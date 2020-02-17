package com.aisdigital.retrofit2.mock.example.di

import com.aisdigital.retrofit2.mock.example.data.LoginDataSource
import com.aisdigital.retrofit2.mock.example.data.LoginRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LoginRepository> { LoginRepository(LoginDataSource()) }
}
