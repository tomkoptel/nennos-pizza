package com.sample.nennos.widget

import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.sample.nennos.R


class CustomSnackbar {
    @ColorRes
    var backgroundColor: Int? = null
    @ColorRes
    var textColor: Int? = null
    var alignTextCenter: Boolean? = null

    fun make(view: View, @StringRes textRes: Int, duration: Int): Snackbar {
        val snackbar = Snackbar.make(view, textRes, duration)

        val snackbarView = snackbar.view
        backgroundColor?.let {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, it))
        }

        val textView = snackbarView.findViewById(R.id.snackbar_text) as TextView
        textColor?.let {
            textView.setTextColor(ContextCompat.getColor(view.context, it))
        }

        alignTextCenter?.let { align ->
            if (align) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                } else {
                    textView.gravity = Gravity.CENTER_HORIZONTAL
                }
            }
        }

        return snackbar
    }

    companion object {
        fun make(view: View, @StringRes textRes: Int, duration: Int): Snackbar {
            return CustomSnackbar().apply {
                backgroundColor = R.color.colorSecondary
                textColor = android.R.color.white
                alignTextCenter = true
            }.make(view, textRes, duration)
        }
    }
}