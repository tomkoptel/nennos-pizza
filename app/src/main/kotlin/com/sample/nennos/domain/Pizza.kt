package com.sample.nennos.domain

import java.util.*

data class Pizza(
        override val id: String = UUID.randomUUID().toString(),
        override val name: String,
        var basePrice: Double = 0.0,
        val imageUrl: String? = null,
        val ingredients: List<Ingredient>
) : Item {
    override val price by lazy(LazyThreadSafetyMode.NONE) {
        basePrice + ingredients.map(Ingredient::price).reduce { left, right -> left + right }
    }

    val ingredientNames by lazy(LazyThreadSafetyMode.NONE) {
        ingredients.map(Ingredient::name).joinToString()
    }
}