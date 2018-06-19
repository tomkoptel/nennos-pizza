package com.sample.nennos.net

import com.sample.nennos.domain.Drink
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DrinkData(
        @Json(name = "price")
        val price: Double,

        @Json(name = "name")
        val name: String,

        @Json(name = "id")
        val id: Int
)

fun DrinkData.toDomainObject() = Drink(remoteId = id, name = name, price = price)