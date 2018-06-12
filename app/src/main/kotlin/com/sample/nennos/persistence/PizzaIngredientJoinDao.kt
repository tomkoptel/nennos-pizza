package com.sample.nennos.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PizzaIngredientJoinDao : BaseDao<PizzaIngredientEntity> {
    @Query(
            """
        SELECT * FROM Ingredient INNER JOIN PizzaIngredient
        ON Ingredient.uid = PizzaIngredient.ingredientId
        WHERE PizzaIngredient.pizzaId = :pizzaId
        """
    )
    fun getIngredientsForPizza(pizzaId: String): List<IngredientEntity>
}