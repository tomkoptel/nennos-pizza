package com.sample.nennos

import android.app.Application
import com.sample.nennos.domain.domainModule
import com.sample.nennos.net.netModule
import com.sample.nennos.persistence.dbModule
import com.sample.nennos.rx.AppSchedulerProvider
import com.sample.nennos.rx.AppSchedulers
import com.squareup.picasso.Picasso
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidModule(this@MainApp))
        import(dbModule)
        import(domainModule)
        import(netModule)
        bind<AppSchedulers>() with provider { AppSchedulerProvider }
        bind<Picasso>() with singleton {
            Picasso.Builder(instance()).build().apply { isLoggingEnabled = BuildConfig.DEBUG }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.tag("NennoPizzaApp")
        Timber.plant(DebugTree())
    }
}
