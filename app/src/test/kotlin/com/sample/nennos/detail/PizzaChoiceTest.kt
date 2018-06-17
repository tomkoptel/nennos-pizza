package com.sample.nennos.detail

import com.sample.nennos.domain.Ingredient
import com.sample.nennos.domain.Pizza
import com.sample.nennos.ktx.toChoice
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.Test

class PizzaChoiceTest {
    @Test
    fun test_toChoice_mapping() {
        val pizza = Pizza(name = "Any", basePrice = 10.0, ingredients = listOf(
                Ingredient(name = "ingredient 1", price = 1.0),
                Ingredient(name = "ingredient 1", price = 2.0)
        ))
        val choice = pizza.toChoice()

        choice.price `should be equal to` 13.0
        choice.ingredients.forEach { it.checked `should be` true }
    }

    @Test
    fun test_copyWithNewIngredient() {
        val pizza = Pizza(name = "Any", basePrice = 10.0, ingredients = listOf(
                Ingredient(name = "ingredient 1", price = 1.0),
                Ingredient(name = "ingredient 1", price = 2.0)
        ))
        val choice = pizza.toChoice()
        val ingredient = choice.ingredients.first()
        val newIngredient = ingredient.copy(checked = false)

        val newChoice = choice.copyWithNewIngredient(newIngredient)!!
        newChoice.price `should be equal to` 12.0
    }
}