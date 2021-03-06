package com.sample.nennos.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
interface IngredientDao : BaseDao<IngredientEntity> {
    @Query("SELECT * FROM Ingredient")
    fun getIngredients(): List<IngredientEntity>

    @Query("SELECT * FROM Ingredient WHERE uid IN (:ids)")
    fun getIngredients(ids: List<String>): List<IngredientEntity>
}