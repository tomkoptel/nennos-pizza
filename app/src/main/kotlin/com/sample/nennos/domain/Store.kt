package com.sample.nennos.domain

import io.reactivex.Completable
import io.reactivex.Single

interface Store<T> {
    fun insertAll(pizzas: List<T>): Completable

    fun getAll(): Single<LookupOperation<List<T>>>

    fun findById(pizzaId: String): Single<LookupOperation<T>>

    enum class Type {
        DISK, NETWORK;
    }
}