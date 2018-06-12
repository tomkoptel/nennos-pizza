package com.sample.nennos.net

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientData(
        @Json(name = "price")
        val price: String,

        @Json(name = "name")
        val name: String,

        @Json(name = "id")
        val id: String
)