package com.sample.nennos.domain

import java.util.*

data class Pizza(
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val imageUrl: String? = null,
        var basePrice: Double = 0.0,
        val ingredients: List<Ingredient>
) {
    val price by lazy(LazyThreadSafetyMode.NONE) {
        basePrice + ingredients.map(Ingredient::price).reduce { left, right -> left + right }
    }

    val ingredientNames by lazy(LazyThreadSafetyMode.NONE) {
        ingredients.map(Ingredient::name).joinToString()
    }
}

fun Pizza.toCartItem() = Item(price = price, name = name)