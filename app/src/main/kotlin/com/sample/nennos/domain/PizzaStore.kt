package com.sample.nennos.domain

import io.reactivex.Completable
import io.reactivex.Single

interface PizzaStore {
    fun insertAll(pizzas: List<Pizza>): Completable

    fun getAll(): Single<LookupOperation<List<Pizza>>>

    fun findById(pizzaId: String): Single<LookupOperation<Pizza>>

    enum class Type {
        DISK, NETWORK;
    }
}