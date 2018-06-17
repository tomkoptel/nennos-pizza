package com.sample.nennos.ktx

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity

fun Context.asActivity(): FragmentActivity {
    if (this is ContextWrapper) {
        return this as? FragmentActivity ?: this.baseContext.asActivity()
    }
    throw IllegalStateException("Is it even possible?")
}