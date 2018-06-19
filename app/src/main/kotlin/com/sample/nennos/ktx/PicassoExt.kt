package com.sample.nennos.ktx

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.sample.nennos.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.lang.Exception

fun ImageView.load(picasso: Picasso, url: String?) {
    if (url == null) {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.custom))
    } else {
        // Troubleshot the issue of [Picasso] on Kitkat platform
        val src = if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
            url
        } else {
            url.replace("https", "http")
        }

        picasso.load(src)
                .placeholder(R.drawable.custom)
                .error(R.drawable.custom)
                .into(this, object : Callback {
                    override fun onSuccess() {
                    }

                    override fun onError(e: Exception?) {
                        e?.let { Timber.e(e) }
                    }
                })
    }
}