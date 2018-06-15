package com.sample.nennos.domain

import com.sample.nennos.persistence.NennoDataBase
import com.sample.nennos.persistence.RoomPizzaRepo
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val domainModule = Kodein.Module {
    bind<PizzaRepo>() with provider { RoomPizzaRepo(instance<NennoDataBase.Factory>()) }
}