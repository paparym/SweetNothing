package com.prestigerito.sweetnothing.android

import android.app.Application
import com.prestigerito.sweetnothing.di.appModule
import com.prestigerito.sweetnothing.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}
