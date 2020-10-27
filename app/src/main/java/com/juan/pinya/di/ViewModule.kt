package com.juan.pinya.di

import com.juan.pinya.view.login.LoginViewModel
import com.juan.pinya.view.main.dailyReport.DailyReportViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { LoginViewModel(get(), get(SHARED_PREFERENCES_NAME)) }
    viewModel {
        DailyReportViewModel(
            stuffRepository = get(),
            sharedPreferencesManager = get(SHARED_PREFERENCES_NAME)
        )
    }
}