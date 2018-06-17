package com.sample.nennos.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "Cart")
data class CartEntity(
        @PrimaryKey var uid: String,
        var createdAt: OffsetDateTime,
        var checkedOutAt: OffsetDateTime?
) {
    constructor() : this("", OffsetDateTime.now(), OffsetDateTime.now())
}