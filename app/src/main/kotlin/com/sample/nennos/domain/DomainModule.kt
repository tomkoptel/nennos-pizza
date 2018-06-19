package com.sample.nennos.domain

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val domainModule = Kodein.Module(name = "domain-module") {
    bind<Repo<Pizza>>(Pizza::class) with singleton {
        DiskNetworkStore<Pizza>(
                diskStore = instance(Store.Type.DISK),
                netStore = instance(Store.Type.NETWORK)
        )
    }
    bind<Repo<Drink>>(Drink::class) with singleton {
        DiskNetworkStore<Drink>(
                diskStore = instance(Store.Type.DISK),
                netStore = instance(Store.Type.NETWORK)
        )
    }
}