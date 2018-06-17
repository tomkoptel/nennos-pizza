package com.sample.nennos.domain

import org.threeten.bp.OffsetDateTime
import java.util.*

data class Cart(
        val id: String = UUID.randomUUID().toString(),
        val createdAt: OffsetDateTime = OffsetDateTime.now(),
        val checkedOutAt: OffsetDateTime? = null,
        val items: List<Item> = emptyList()) {
    companion object {
        val NULL = Cart("NULL")
    }

    val price: Double by lazy(LazyThreadSafetyMode.NONE) {
        items.map { it.price }.reduce { right, left -> right + left }
    }
}