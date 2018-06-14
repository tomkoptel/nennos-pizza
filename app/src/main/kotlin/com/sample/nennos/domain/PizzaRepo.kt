package com.sample.nennos.domain

import io.reactivex.Completable
import io.reactivex.Single

interface PizzaRepo {
    fun insertAll(pizzas: List<Pizza>): Completable

    fun getAll(): Single<List<Pizza>>
}