package com.sample.nennos.domain

import java.util.*

data class Drink(
        override val id: String = UUID.randomUUID().toString(),
        override val name: String = "",
        override val price: Double = 0.0,
        val remoteId: Int
) : Item