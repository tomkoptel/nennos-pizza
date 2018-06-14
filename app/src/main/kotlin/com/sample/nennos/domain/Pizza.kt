package com.sample.nennos.domain

data class Pizza(
        val id: Int,
        val name: String,
        val imageUrl: String?,
        val ingredients: List<Ingredient>
)