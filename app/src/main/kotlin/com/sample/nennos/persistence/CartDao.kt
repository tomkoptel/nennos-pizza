package com.sample.nennos.persistence

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface CartDao {
    @Transaction
    @Query("SELECT * FROM Cart ORDER BY datetime(createdAt)")
    fun getCarts(): List<CartAndItems>

    @Transaction
    @Query("SELECT * FROM Cart WHERE Cart.checkedOutAt is NULL ORDER BY datetime(createdAt)")
    fun getRecentCarts(): Flowable<List<CartAndItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(entities: List<ItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(entity: ItemEntity)

    @Delete
    fun removeItem(entity: ItemEntity)

    @Transaction
    fun insertCart(entity: CartAndItems) {
        entity.cart?.let { cart ->
            insert(cart)
            insertItems(entity.items)
        }
    }
}