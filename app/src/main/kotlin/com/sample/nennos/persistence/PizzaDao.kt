package com.sample.nennos.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface PizzaDao {
    @Query("SELECT * FROM Pizza")
    fun getPizzas(): List<PizzaEntity>

    @Insert(onConflict = REPLACE)
    fun insert(pizza: PizzaEntity)
}