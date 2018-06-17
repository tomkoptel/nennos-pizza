package com.sample.nennos.domain

import org.threeten.bp.OffsetDateTime
import java.util.*

data class Item(
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val price: Double,
        val createdAt: OffsetDateTime = OffsetDateTime.now()
)