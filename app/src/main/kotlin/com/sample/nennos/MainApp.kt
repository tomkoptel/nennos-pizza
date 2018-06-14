package com.sample.nennos

import android.app.Application
import android.content.Context
import com.sample.nennos.data.dbModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainApp : Application(), KodeinAware {
    private val app = this

    override val kodein = Kodein.lazy {
        bind<Context>() with provider { app }
        import(dbModule)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}
