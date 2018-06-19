package com.sample.nennos.domain

import java.util.*

data class Ingredient(
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val price: Double,
        val remoteId: Int
)