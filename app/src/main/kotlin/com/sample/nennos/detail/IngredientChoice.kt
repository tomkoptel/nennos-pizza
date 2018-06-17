package com.sample.nennos.detail

import com.sample.nennos.domain.Ingredient

data class IngredientChoice(val ingredient: Ingredient, val checked: Boolean) {
    val price: Double get() = if (checked) ingredient.price else 0.0
}