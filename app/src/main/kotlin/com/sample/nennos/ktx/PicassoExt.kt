package com.sample.nennos.ktx

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.load(picasso: Picasso, url: String?) {
    url?.let {
        picasso.load(url).into(this)
    }
}