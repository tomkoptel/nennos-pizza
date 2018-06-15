package com.sample.nennos.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PizzaIngredientJoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entities: List<PizzaIngredientEntity>)

    @Query(
            """
        SELECT * FROM Ingredient INNER JOIN PizzaIngredient
        ON Ingredient.uid = PizzaIngredient.ingredientId
        WHERE PizzaIngredient.pizzaId = :pizzaId
        """
    )
    fun getIngredientsForPizza(pizzaId: String): List<IngredientEntity>

}