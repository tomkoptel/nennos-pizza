package com.sample.nennos.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Ingredient",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = PizzaEntry::class,
                        parentColumns = arrayOf("uid"),
                        childColumns = arrayOf("pizzaId"),
                        onDelete = CASCADE
                )
        )
)
data class IngredientEntity @JvmOverloads constructor(
        @PrimaryKey var uid: String = UUID.randomUUID().toString(),
        var pizzaId: String? = null,
        var name: String = "",
        var price: Double = 0.0
)