package com.sample.nennos.domain

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val domainModule = Kodein.Module {
    bind<PizzaRepo>() with singleton {
        PizzaRepoImpl(
                diskStore = instance(PizzaStore.Type.DISK),
                netStore = instance(PizzaStore.Type.NETWORK)
        )
    }
}