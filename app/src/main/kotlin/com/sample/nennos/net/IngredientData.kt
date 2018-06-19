package com.sample.nennos.net

import com.sample.nennos.domain.Ingredient
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientData(
        @Json(name = "name")
        val name: String,

        @Json(name = "price")
        val price: Double,

        @Json(name = "id")
        val id: Int
)

fun IngredientData.toDomainObject() = Ingredient(price = price, name = name, remoteId = id)