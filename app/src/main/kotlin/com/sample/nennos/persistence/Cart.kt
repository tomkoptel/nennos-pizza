package com.sample.nennos.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

private const val CART_ID_KEY = "cartId"
private const val INGREDIENT_ID_KEY = "ingredientId"
private const val PIZZA_ID_KEY = "pizzaId"
private const val DRINK_ID_KEY = "drinkId"
private const val UID_KEY = "uid"

@Entity(
        tableName = "CartIngredient",
        primaryKeys = arrayOf(CART_ID_KEY, INGREDIENT_ID_KEY, PIZZA_ID_KEY),
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = CartEntity::class,
                        parentColumns = arrayOf(UID_KEY),
                        childColumns = arrayOf(CART_ID_KEY),
                        onDelete = ForeignKey.CASCADE
                ),
                ForeignKey(
                        entity = IngredientEntity::class,
                        parentColumns = arrayOf(UID_KEY),
                        childColumns = arrayOf(INGREDIENT_ID_KEY),
                        onDelete = ForeignKey.CASCADE
                ),
                ForeignKey(
                        entity = PizzaEntity::class,
                        parentColumns = arrayOf(UID_KEY),
                        childColumns = arrayOf(PIZZA_ID_KEY),
                        onDelete = ForeignKey.CASCADE
                )
        )
)
data class CartIngredientEntity(
        @ColumnInfo(name = CART_ID_KEY) var cartId: String = "",
        @ColumnInfo(name = INGREDIENT_ID_KEY) var ingredientId: String = "",
        @ColumnInfo(name = PIZZA_ID_KEY) var pizzaId: String = ""
)

@Entity(
        tableName = "CartPizza",
        primaryKeys = arrayOf(CART_ID_KEY, PIZZA_ID_KEY),
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = CartEntity::class,
                        parentColumns = arrayOf(UID_KEY),
                        childColumns = arrayOf(CART_ID_KEY),
                        onDelete = ForeignKey.CASCADE
                ),
                ForeignKey(
                        entity = PizzaEntity::class,
                        parentColumns = arrayOf(UID_KEY),
                        childColumns = arrayOf(PIZZA_ID_KEY),
                        onDelete = ForeignKey.CASCADE
                )
        )
)
data class CartPizzaEntity(
        @ColumnInfo(name = CART_ID_KEY) val cartId: String,
        @ColumnInfo(name = PIZZA_ID_KEY) val pizzaId: String
)

@Entity(
        tableName = "CartDrink",
        primaryKeys = arrayOf(CART_ID_KEY, DRINK_ID_KEY),
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = CartEntity::class,
                        parentColumns = arrayOf(UID_KEY),
                        childColumns = arrayOf(CART_ID_KEY),
                        onDelete = ForeignKey.CASCADE
                ),
                ForeignKey(
                        entity = DrinkEntity::class,
                        parentColumns = arrayOf(UID_KEY),
                        childColumns = arrayOf(DRINK_ID_KEY),
                        onDelete = ForeignKey.CASCADE
                )
        )
)
data class CartDrinkEntity(
        @ColumnInfo(name = CART_ID_KEY) val cartId: String,
        @ColumnInfo(name = DRINK_ID_KEY) val drinkId: String
)