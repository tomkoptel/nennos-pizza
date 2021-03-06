package com.sample.nennos.ktx

import com.sample.nennos.detail.IngredientChoice
import com.sample.nennos.detail.PizzaChoice
import com.sample.nennos.detail.PizzaDetails
import com.sample.nennos.domain.*
import java.text.NumberFormat
import java.util.*

private fun formatPrice(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        currency = Currency.getInstance("USD")
    }.format(price)
}

fun Ingredient.formattedPrice(): String {
    return formatPrice(price)
}

fun Pizza.formattedPrice(): String {
    return formatPrice(price)
}

fun PizzaChoice.formattedPrice(): String {
    return formatPrice(price)
}

fun PizzaDetails.formattedPrice(): String {
    return formatPrice(price)
}

fun Cart.formattedPrice(): String {
    return formatPrice(price)
}

fun Item.formattedPrice(): String {
    return formatPrice(price)
}

fun Drink.formattedPrice(): String {
    return formatPrice(price)
}

fun Pizza.toChoice(): PizzaChoice {
    val ingredients = ingredients.map { IngredientChoice(it, false) }
    return PizzaChoice(this, ingredients).apply { price }
}

fun Pizza.toParcelable(): PizzaDetails {
    return PizzaDetails(id = id, name = name, price = price, imageUrl = imageUrl)
}
