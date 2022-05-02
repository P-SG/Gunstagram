package com.psg.gunstagram

import android.app.Application
import com.psg.gunstagram.data.di.appModule
import com.psg.gunstagram.data.di.repositoryModule
import com.psg.gunstagram.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Gunstagram: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@Gunstagram)
            modules(listOf(appModule, viewModelModule, repositoryModule))
        }
    }
}