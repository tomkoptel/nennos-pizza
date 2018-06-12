package com.sample.nennos

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}
