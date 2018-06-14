package com.sample.nennos.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Ingredient")
data class IngredientEntity @JvmOverloads constructor(
        @PrimaryKey var uid: String = UUID.randomUUID().toString(),
        var name: String = "",
        var price: Double = 0.0
)