package com.sample.nennos.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PizzaDao : BaseDao<PizzaEntity> {
    @Query("SELECT * FROM Pizza WHERE Pizza.visible = 1")
    fun getPizzas(): List<PizzaEntity>

    @Query("SELECT * FROM Pizza WHERE Pizza.uid=:pizzaId")
    fun findPizzaById(pizzaId: String): PizzaEntity
}