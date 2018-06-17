package com.sample.nennos.domain

import io.reactivex.Flowable
import io.reactivex.Single

interface CartRepo {
    fun updateOrCreateCart(item: Item): Single<Item>

    fun removeItemFromCart(item: Item): Single<Item>

    fun getRecentCart(): Flowable<Cart>
}