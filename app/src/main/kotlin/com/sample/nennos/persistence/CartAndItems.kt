package com.sample.nennos.persistence

import androidx.room.Embedded
import androidx.room.Relation
import com.sample.nennos.domain.Cart

class CartAndItems {
    @Embedded
    var cart: CartEntity? = null

    @Relation(parentColumn = "uid", entityColumn = "cartId")
    var items: List<ItemEntity> = emptyList()
}

fun Cart.toDataObject(): CartAndItems {
    val cartEntity = CartEntity(uid = id, createdAt = createdAt, checkedOutAt = checkedOutAt)
    val itemEntites = items.map { it.toDataObject(cartEntity.uid) }
    return CartAndItems().apply {
        cart = cartEntity
        items = itemEntites
    }
}

fun CartAndItems.toDomainObject(): Cart {
    val localCart = checkNotNull(cart) { "Cart should not be null. Error during mapping of entity!" }
    val items = items.map(ItemEntity::toDomainObject)

    return localCart.run {
        Cart(id = uid, createdAt = createdAt, checkedOutAt = checkedOutAt, items = items)
    }
}