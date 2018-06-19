package com.sample.nennos.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.nennos.domain.Ingredient
import java.util.*

@Entity(tableName = "Ingredient")
data class IngredientEntity @JvmOverloads constructor(
        @PrimaryKey var uid: String = UUID.randomUUID().toString(),
        var name: String = "",
        var price: Double = 0.0,
        var remoteId: Int = 0
)

fun IngredientEntity.toDomainObject() = Ingredient(id = uid, name = name, price = price, remoteId = remoteId)

fun Ingredient.toDataObject() = IngredientEntity(uid = id, name = name, price = price, remoteId = remoteId)