package com.sample.nennos.domain

import java.util.*

data class Drink(
        val id: String = UUID.randomUUID().toString(),
        val name: String = "",
        val price: Double = 0.0,
        val remoteId: String
)

fun Drink.toCartItem() = Item(price = price, name = name)