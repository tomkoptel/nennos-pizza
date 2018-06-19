package com.sample.nennos.domain

import org.threeten.bp.OffsetDateTime
import java.util.*

data class Cart(
        val id: String = UUID.randomUUID().toString(),
        val createdAt: OffsetDateTime = OffsetDateTime.now(),
        val checkedOutAt: OffsetDateTime? = null,
        val drinks: List<Drink> = emptyList(),
        val pizzas: List<Pizza> = emptyList()) {
    companion object {
        val NULL = Cart("NULL")
    }

    val price: Double by lazy(LazyThreadSafetyMode.NONE) {
        reducePrice(drinks) + reducePrice(pizzas)
    }

    val items: List<Item> by lazy(LazyThreadSafetyMode.NONE) {
        pizzas + drinks
    }

    val size: Int = drinks.size + pizzas.size

    private fun reducePrice(items: List<Item>): Double {
        return if (items.isEmpty()) 0.0 else items.map(Item::price).reduce { r, l -> r + l }
    }
}