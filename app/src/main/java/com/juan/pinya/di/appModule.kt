package com.juan.pinya.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.StuffDao
import com.juan.pinya.module.StuffDaoImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val SHARED_PREFERENCES = "pinya_shared_preferences"
val SHARED_PREFERENCES_NAME = named(SHARED_PREFERENCES)

val appModule = module {
    single { GsonBuilder().setLenient().serializeNulls().setPrettyPrinting().create() }
    single<StuffDao> { StuffDaoImpl(get()) }

    single(SHARED_PREFERENCES_NAME) {
        androidContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }
    single(SHARED_PREFERENCES_NAME) {
        SharedPreferencesManager(get(SHARED_PREFERENCES_NAME))
    }
}