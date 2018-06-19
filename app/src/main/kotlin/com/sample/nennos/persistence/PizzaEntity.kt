package com.sample.nennos.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.nennos.domain.Ingredient
import com.sample.nennos.domain.Pizza
import java.util.*

@Entity(tableName = "Pizza")
data class PizzaEntity @JvmOverloads constructor(
        @PrimaryKey var uid: String = UUID.randomUUID().toString(),
        var name: String = "",
        var basePrice: Double = 0.0,
        var imageUrl: String? = null,
        var visible: Boolean = true
)

fun Pizza.toDataObject(): PizzaEntity = PizzaEntity(
        uid = id,
        name = name,
        imageUrl = imageUrl,
        basePrice = basePrice,
        visible = visible
)

fun PizzaEntity.toDomainObject(ingredients: List<Ingredient>): Pizza {
    return Pizza(id = uid,
            name = name,
            imageUrl = imageUrl,
            ingredients = ingredients,
            basePrice = basePrice,
            visible = visible
    )
}