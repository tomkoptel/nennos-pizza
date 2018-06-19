package com.sample.nennos.domain

import org.amshove.kluent.`should be equal to`
import org.junit.Test

class PizzaTest {
    @Test
    fun calculate_pizza_price() {
        Pizza(
                name = "Any",
                basePrice = 10.0,
                ingredients = listOf(
                        Ingredient(name = "any", price = 1.5, remoteId = 1),
                        Ingredient(name = "any", price = 2.5, remoteId = 2)
                )
        ).price `should be equal to` 14.0
    }
}