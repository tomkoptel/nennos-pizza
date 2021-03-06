package com.sample.nennos.net

import com.sample.nennos.domain.Pizza
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PizzaData(
        @Json(name = "name")
        val name: String,

        @Json(name = "ingredients")
        val ingredients: List<Int> = emptyList(),

        @Json(name = "imageUrl")
        val imageUrl: String? = null
)

fun Pizza.toDataObject(): PizzaData {
    val ingredients = ingredients.map { it.remoteId }
    return PizzaData(name = name, imageUrl = imageUrl, ingredients = ingredients)
}