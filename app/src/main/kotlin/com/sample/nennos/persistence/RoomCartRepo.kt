package com.sample.nennos.persistence

import com.sample.nennos.domain.Cart
import com.sample.nennos.domain.CartRepo
import com.sample.nennos.domain.Item
import io.reactivex.Flowable
import io.reactivex.Single

class RoomCartRepo(private val dbProvider: () -> Single<NennoDataBase>) : CartRepo {
    constructor(factory: NennoDataBase.Factory) : this({
        factory.getDatabase()
    })

    override fun removeItemFromCart(item: Item): Single<Item> {
        return dbProvider().flatMap { database ->
            getRecentCart().take(1)
                    .flatMapSingle {
                        database.cartDao().removeItem(item.toDataObject(cartId = it.id))
                        Single.just(item)
                    }.singleOrError()
        }
    }

    override fun updateOrCreateCart(item: Item): Single<Item> {
        return dbProvider().flatMap { database ->
            getRecentCart().take(1).flatMapSingle {
                val entity = if (it == Cart.NULL) {
                    Cart(items = listOf(item)).toDataObject()
                } else {
                    val newItems = it.items.toMutableList()
                    newItems.add(item)
                    it.copy(items = newItems).toDataObject()
                }

                database.cartDao().insertCart(entity)
                Single.just(item)
            }.singleOrError()
        }
    }

    override fun getRecentCart(): Flowable<Cart> {
        return dbProvider()
                .flatMapPublisher {
                    it.cartDao().getRecentCarts()
                            .distinctUntilChanged()
                            .map { recentCarts ->
                                if (recentCarts.isEmpty()) {
                                    Cart.NULL
                                } else {
                                    recentCarts.last().toDomainObject()
                                }
                            }
                }
    }
}