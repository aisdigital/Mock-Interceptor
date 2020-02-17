package com.aisdigital.retrofit2.mock.example.di

import com.aisdigital.retrofit2.mock.example.ui.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
}