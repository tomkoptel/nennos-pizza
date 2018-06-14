package com.sample.nennos.domain

import io.reactivex.Completable
import io.reactivex.Single

interface PizzaRepo {
    fun insertAll(pizzas: List<Pizza>): Completable

    fun getAll(): Single<LookupOperation<List<Pizza>>>
}

sealed class LookupOperation<out R> {
    class Success<out R>(val data: R) : LookupOperation<R>()
    class Error<out R>(val error: Throwable) : LookupOperation<R>()
}