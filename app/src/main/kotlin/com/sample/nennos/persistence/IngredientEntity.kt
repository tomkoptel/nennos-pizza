package com.sample.nennos.persistence

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.sample.nennos.domain.Ingredient
import java.util.*

@Entity(tableName = "Ingredient",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = PizzaEntity::class,
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

fun IngredientEntity.toDomainObject(): Ingredient = Ingredient(id = uid, name = name, price = price)

fun Ingredient.toDataObject(pizza: PizzaEntity): IngredientEntity =
        IngredientEntity(uid = id, name = name, price = price, pizzaId = pizza.uid)