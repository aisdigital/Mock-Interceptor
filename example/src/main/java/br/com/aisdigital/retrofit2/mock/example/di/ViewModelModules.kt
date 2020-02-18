package br.com.aisdigital.retrofit2.mock.example.di

import br.com.aisdigital.retrofit2.mock.example.ui.home.viewmodel.HomeViewModel
import br.com.aisdigital.retrofit2.mock.example.ui.login.viewmodel.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}