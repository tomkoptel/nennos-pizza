package com.sample.nennos.persistence

import com.sample.nennos.domain.CartRepo
import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.Pizza
import com.sample.nennos.domain.Store
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val dbModule = Kodein.Module {
    bind<NennoDataBase.Factory>() with singleton { NennoDataBase.Factory(instance()) }
    bind<Store<Pizza>>(Store.Type.DISK) with singleton { RoomPizzaStore(instance<NennoDataBase.Factory>()) }
    bind<Store<Drink>>(Store.Type.DISK) with singleton { RoomDrinkStore(instance<NennoDataBase.Factory>()) }
    bind<CartRepo>() with singleton { RoomCartRepo(instance(), instance<NennoDataBase.Factory>()) }
}