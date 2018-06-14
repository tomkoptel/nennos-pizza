package com.sample.nennos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface DrinkDao {
    @Query("SELECT * FROM Drink")
    fun getDrinks(): List<DrinkEntity>

    @Insert(onConflict = REPLACE)
    fun insertAll(pizzas: List<DrinkEntity>)
}