package com.sample.nennos.persistence

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val dbModule = Kodein.Module {
    bind<NennoDataBase.Factory>() with singleton { NennoDataBase.Factory(instance()) }
}