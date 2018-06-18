package com.sample.nennos.net

import com.sample.nennos.domain.Pizza
import com.sample.nennos.domain.Store
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val netModule = Kodein.Module {
    bind<ApiService>() with singleton { ApiService.create() }
    bind<Store<Pizza>>(Store.Type.NETWORK) with singleton { NetPizzaStore(instance()) }
}