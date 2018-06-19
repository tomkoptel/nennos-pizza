package com.sample.nennos.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.nennos.domain.Cart
import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.Pizza
import org.threeten.bp.OffsetDateTime
import java.util.*

@Entity(tableName = "Cart")
data class CartEntity @JvmOverloads constructor(
        @PrimaryKey var uid: String = UUID.randomUUID().toString(),
        var createdAt: OffsetDateTime = OffsetDateTime.now(),
        var checkedOutAt: OffsetDateTime? = null
)

fun CartEntity.toDomainObject(pizzas: List<Pizza>, drinks: List<Drink>) = Cart(id = uid, createdAt = createdAt, checkedOutAt = checkedOutAt, drinks = drinks, pizzas = pizzas)

fun Cart.toDataObject() = CartEntity(uid = id, createdAt = createdAt, checkedOutAt = checkedOutAt)