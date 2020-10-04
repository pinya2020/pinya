package com.juan.pinya

import android.app.Application
import com.juan.pinya.di.appModule
import com.juan.pinya.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class PinyaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@PinyaApplication)
            loadKoinModules(
                listOf(
                    appModule,
                    viewModule
                )
            )
        }
    }
}