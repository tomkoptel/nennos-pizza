package com.sample.nennos.detail

import com.sample.nennos.domain.Pizza

data class PizzaChoice(private val pizza: Pizza, val ingredients: List<IngredientChoice>) {
    val price by lazy(LazyThreadSafetyMode.NONE) {
        val ingredientsPrice = ingredients.map { it.price }.reduce { right, left -> right + left }
        ingredientsPrice + pizza.basePrice
    }

    fun toDomainObject(): Pizza {
        val checkedIngredients = ingredients.filter { it.checked }.map { it.ingredient }
        return pizza.copy(ingredients = checkedIngredients)
    }

    fun copyWithNewIngredient(choice: IngredientChoice): PizzaChoice? {
        val previousIngredient = ingredients.find { it.ingredient == choice.ingredient }
                ?: return null

        val newIngredientList = ingredients.toMutableList()
        val index = newIngredientList.indexOf(previousIngredient)
        newIngredientList[index] = choice

        return copy(ingredients = newIngredientList).apply { price }
    }
}