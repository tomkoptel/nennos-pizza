package com.sample.nennos.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface CartRepo {
    fun updateOrCreateCart(item: Pizza): Single<Pizza>

    fun updateOrCreateCart(item: Drink): Single<Drink>

    fun removeItemFromCart(item: Pizza): Single<Pizza>

    fun removeItemFromCart(item: Drink): Single<Drink>

    fun getRecentCart(): Flowable<Cart>

    fun checkOut(cart: Cart): Completable
}