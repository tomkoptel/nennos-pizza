package com.sample.nennos.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.nennos.domain.Drink
import java.util.*

@Entity(tableName = "Drink")
data class DrinkEntity @JvmOverloads constructor(
        @PrimaryKey var uid: String = UUID.randomUUID().toString(),
        var name: String = "",
        var price: Double = 0.0,
        var remoteId: String
)

fun Drink.toDataEntity() = DrinkEntity(uid = id, name = name, price = price, remoteId = remoteId)

fun DrinkEntity.toDomainObject() = Drink(id = uid, name = name, price = price, remoteId = remoteId)