package com.sample.nennos.net

import com.sample.nennos.domain.Cart
import com.sample.nennos.domain.Pizza
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CartData(
        @Json(name = "price")
        val pizzas: List<PizzaData> = emptyList(),
        @Json(name = "price")
        val drinks: List<Int> = emptyList()
)

fun Cart.toNetDataObject(): CartData {
    val pizzas = pizzas.map(Pizza::toDataObject)
    val drinks = drinks.map { it.remoteId }
    return CartData(pizzas = pizzas, drinks = drinks)
}