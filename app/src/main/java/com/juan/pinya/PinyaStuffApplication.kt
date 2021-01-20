package com.juan.pinya

import android.app.Application
import com.juan.pinya.di.appModule
import com.juan.pinya.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class PinyaStuffApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@PinyaStuffApplication)
            loadKoinModules(
                listOf(
                    appModule,
                    viewModule
                )
            )
        }
    }
}