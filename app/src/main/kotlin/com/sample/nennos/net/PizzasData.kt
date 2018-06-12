package com.sample.nennos.net

import com.squareup.moshi.Json

data class PizzasData(
        @Json(name = "pizzas")
        val pizzas: List<PizzaData> = emptyList(),

        @Json(name = "basePrice")
        val basePrice: Double
)