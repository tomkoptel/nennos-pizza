package com.sample.nennos.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DrinkDao : BaseDao<DrinkEntity> {
    @Query("SELECT * FROM Drink")
    fun getDrinks(): List<DrinkEntity>
}