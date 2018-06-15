package com.sample.nennos

import android.app.Application
import com.sample.nennos.persistence.dbModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidModule(this@MainApp))
        import(dbModule)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}
