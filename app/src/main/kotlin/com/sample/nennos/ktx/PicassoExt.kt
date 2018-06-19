package com.sample.nennos.ktx

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.sample.nennos.R
import com.squareup.picasso.Picasso

fun ImageView.load(picasso: Picasso, url: String?) {
    if (url == null) {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.custom))
    } else {
        picasso.load(url)
                .placeholder(R.drawable.custom)
                .error(R.drawable.custom)
                .into(this)
    }
}