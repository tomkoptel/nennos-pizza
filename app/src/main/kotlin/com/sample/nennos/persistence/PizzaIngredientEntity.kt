package com.sample.nennos.persistence

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
        tableName = "PizzaIngredient",
        primaryKeys = arrayOf("pizzaId", "ingredientId"),
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = PizzaEntity::class,
                        parentColumns = arrayOf("uid"),
                        childColumns = arrayOf("pizzaId"),
                        onDelete = ForeignKey.CASCADE
                ),
                ForeignKey(
                        entity = IngredientEntity::class,
                        parentColumns = arrayOf("uid"),
                        childColumns = arrayOf("ingredientId"),
                        onDelete = ForeignKey.CASCADE
                )
        )
)
data class PizzaIngredientEntity(
        var pizzaId: String,
        var ingredientId: String
) {
    companion object {
        fun fromMapping(pizzaWithIngredients: Map<PizzaEntity, List<IngredientEntity>>): List<PizzaIngredientEntity> {
            return pizzaWithIngredients.map {
                val (pizzaEntity, ingredientEntities) = it
                ingredientEntities.map {
                    PizzaIngredientEntity(pizzaId = pizzaEntity.uid, ingredientId = it.uid)
                }
            }.flatten()
        }
    }
}