package com.sample.nennos.domain

import java.util.*

data class Pizza(
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val imageUrl: String?,
        var basePrice: Double = 0.0,
        val ingredients: List<Ingredient>
)