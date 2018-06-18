package com.sample.nennos.domain

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val domainModule = Kodein.Module {
    bind<Repo<Pizza>>(Pizza::class) with singleton {
        DiskNetworkStore<Pizza>(
                diskStore = instance(Store.Type.DISK),
                netStore = instance(Store.Type.NETWORK)
        )
    }
}