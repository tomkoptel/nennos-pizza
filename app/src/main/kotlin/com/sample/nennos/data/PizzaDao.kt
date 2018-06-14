package com.sample.nennos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface PizzaDao {
    @Query("SELECT * FROM Pizza")
    fun getPizzas(): List<PizzaEntry>

    @Insert(onConflict = REPLACE)
    fun insertAll(pizzas: List<PizzaEntry>)
}