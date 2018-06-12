package com.sample.nennos.ktx

import com.sample.nennos.domain.Pizza
import java.text.NumberFormat
import java.util.*

fun Pizza.formattedPrice(): String {
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        currency = Currency.getInstance("USD")
    }.format(price)
}