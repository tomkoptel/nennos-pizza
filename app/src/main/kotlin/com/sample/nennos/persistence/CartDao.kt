package com.sample.nennos.persistence

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface CartDao {
    @Transaction
    @Query("SELECT * FROM Cart WHERE Cart.checkedOutAt is NULL ORDER BY datetime(createdAt)")
    fun getRecentCarts(): Flowable<List<CartEntity>>

    @Query(
            """
        SELECT * FROM Pizza INNER JOIN CartPizza
        ON CartPizza.pizzaId = Pizza.uid
        WHERE CartPizza.cartId = :cartId
        """
    )
    fun getPizzasByCartId(cartId: String): List<PizzaEntity>

    @Query(
            """
        SELECT * FROM Ingredient INNER JOIN CartIngredient
        ON Ingredient.uid = CartIngredient.ingredientId
        WHERE CartIngredient.cartId = :cartId AND CartIngredient.pizzaId = :pizzaId
        """
    )
    fun getIngredientsByPizzaAndCartId(cartId: String, pizzaId: String): List<IngredientEntity>

    @Query(
            """
        SELECT * FROM Drink INNER JOIN CartDrink
        ON Drink.uid = CartDrink.drinkId
        WHERE CartDrink.cartId = :cartId
        """
    )
    fun getDrinksByCartId(cartId: String): List<DrinkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCart(entity: CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCart(entity: CartPizzaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredients(entities: List<CartIngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDrink(entity: CartDrinkEntity)


    @Query("SELECT * FROM Cart")
    fun getAllCarts(): List<CartEntity>

    @Query("SELECT * FROM CartPizza")
    fun getAllCartPizzaEntity(): List<CartPizzaEntity>

    @Query("SELECT * FROM CartIngredient")
    fun getAllCartIngredientEntity(): List<CartIngredientEntity>

    @Query("SELECT * FROM CartDrink")
    fun getAllCartDrinkEntity(): List<CartDrinkEntity>

    @Transaction
    fun insertCartPizzaWithIngredients(entity: CartPizzaEntity, entities: List<CartIngredientEntity>) {
        insertCart(entity)
        insertIngredients(entities)
    }

    @Transaction
    fun removePizzaIngredients(cartId: String, pizzaId: String) {
        removePizza(cartId, pizzaId)
        removeIngredients(cartId, pizzaId)
    }

    @Query("DELETE FROM CartPizza WHERE CartPizza.cartId = :cartId AND CartPizza.pizzaId = :pizzaId")
    fun removePizza(cartId: String, pizzaId: String)

    @Query("DELETE FROM CartIngredient WHERE CartIngredient.cartId = :cartId AND CartIngredient.pizzaId = :pizzaId")
    fun removeIngredients(cartId: String, pizzaId: String)

    @Query("DELETE FROM CartDrink WHERE CartDrink.cartId = :cartId AND CartDrink.drinkId = :drinkId")
    fun removeDrink(cartId: String, drinkId: String)

}