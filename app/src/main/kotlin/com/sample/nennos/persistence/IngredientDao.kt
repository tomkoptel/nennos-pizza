package com.sample.nennos.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
interface IngredientDao : BaseDao<IngredientEntity> {
    @Query("SELECT * FROM Ingredient")
    fun getIngredients(): List<IngredientEntity>

    @Query("SELECT * FROM Ingredient WHERE uid in (:ids)")
    fun getIngredients(ids: String): List<IngredientEntity>
}