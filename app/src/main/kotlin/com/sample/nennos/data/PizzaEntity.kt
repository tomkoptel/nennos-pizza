package com.sample.nennos.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.nennos.domain.Pizza
import java.util.*

@Entity(tableName = "Pizza")
data class PizzaEntity @JvmOverloads constructor(
        @PrimaryKey var uid: String = UUID.randomUUID().toString(),
        var name: String = "",
        var basePrice: Double = 0.0,
        var imageUrl: String? = null
)

fun Pizza.toDataObject(): PizzaEntity = PizzaEntity(uid = id, name = name, imageUrl = imageUrl)