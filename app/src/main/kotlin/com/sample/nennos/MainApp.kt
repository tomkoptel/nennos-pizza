package com.sample.nennos

import android.app.Application
import com.sample.nennos.domain.domainModule
import com.sample.nennos.persistence.dbModule
import com.sample.nennos.rx.AppSchedulerProvider
import com.sample.nennos.rx.AppSchedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidModule(this@MainApp))
        import(dbModule)
        import(domainModule)
        bind<AppSchedulers>() with provider { AppSchedulerProvider }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}
