package br.com.aisdigital.retrofit2.mock.example.di

import br.com.aisdigital.retrofit2.mock.example.network.datasource.LoginDataSource
import br.com.aisdigital.retrofit2.mock.example.network.repository.LoginRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        LoginRepository(
            LoginDataSource(get())
        )
    }
}
