package com.sample.nennos.persistence

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sample.nennos.domain.Item
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "Item",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = CartEntity::class,
                        parentColumns = arrayOf("uid"),
                        childColumns = arrayOf("cartId")
                )
        )
)
data class ItemEntity(
        @PrimaryKey var uid: String,
        var name: String,
        var cartId: String,
        var price: Double,
        var createdAt: OffsetDateTime
) {
    constructor() : this("", "", "", 0.0, OffsetDateTime.now())
}

fun Item.toDataObject(cartId: String) = ItemEntity(uid = id, name = name, price = price, cartId = cartId, createdAt = createdAt)

fun ItemEntity.toDomainObject() = Item(id = uid, name = name, price = price, createdAt = createdAt)