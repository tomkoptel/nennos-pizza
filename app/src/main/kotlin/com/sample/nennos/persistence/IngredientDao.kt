package com.sample.nennos.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface IngredientDao {
    @Query("SELECT * FROM Ingredient")
    fun getIngredients(): List<IngredientEntity>

    @Insert(onConflict = REPLACE)
    fun insertAll(pizzas: List<IngredientEntity>)
}