package com.sample.nennos.detail

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class PizzaDetails(
        val id: String,
        val name: String,
        val price: Double,
        val imageUrl: String?
) : Parcelable