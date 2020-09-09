package com.juan.pinya.di

import com.juan.pinya.view.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { LoginViewModel(get(), get(SHARED_PREFERENCES_NAME)) }
}