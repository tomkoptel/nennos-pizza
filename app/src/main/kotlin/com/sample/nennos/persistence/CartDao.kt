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

    @Query("SELECT * FROM CartPizza WHERE CartPizza.cartId = :cartId")
    fun getCartPizzaEntitiesByCartId(cartId: String): List<CartPizzaEntity>

    @Query("SELECT * FROM CartDrink WHERE CartDrink.cartId = :cartId")
    fun getCartDrinkEntitiesByCartId(cartId: String): List<CartDrinkEntity>

    @Query(
            """
        SELECT * FROM Drink INNER JOIN CartDrink
        ON Drink.uid = CartDrink.drinkId
        WHERE CartDrink.cartId = :cartId
        """
    )
    fun getDrinksByCartId(cartId: String): List<DrinkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPizza(entity: CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPizza(entity: CartPizzaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDrink(entity: CartDrinkEntity)

    @Query("SELECT * FROM Cart")
    fun getAllCarts(): List<CartEntity>

    @Query("SELECT * FROM CartPizza")
    fun getAllCartPizzaEntity(): List<CartPizzaEntity>

    @Query("SELECT * FROM CartDrink")
    fun getAllCartDrinkEntity(): List<CartDrinkEntity>

    @Query("DELETE FROM CartPizza WHERE CartPizza.cartId = :cartId AND CartPizza.pid = :pizzaId")
    fun removePizza(cartId: String, pizzaId: String)

    @Query("DELETE FROM CartDrink WHERE CartDrink.cartId = :cartId AND CartDrink.pid = :drinkId")
    fun removeDrink(cartId: String, drinkId: String)

    @Update
    fun updateCart(cartEntity: CartEntity)
}