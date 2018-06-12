package com.sample.nennos.net

import com.squareup.moshi.Json

data class PizzaData(
        @Json(name = "name")
        val name: String,

        @Json(name = "ingredients")
        val ingredients: List<String> = emptyList(),

        @Json(name = "imageUrl")
        val imageUrl: String? = null
)