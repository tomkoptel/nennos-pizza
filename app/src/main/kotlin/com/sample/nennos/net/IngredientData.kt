package com.sample.nennos.net

import com.squareup.moshi.Json

data class IngredientData(
        @Json(name = "price")
        val price: String,

        @Json(name = "name")
        val name: String,

        @Json(name = "id")
        val id: String
)