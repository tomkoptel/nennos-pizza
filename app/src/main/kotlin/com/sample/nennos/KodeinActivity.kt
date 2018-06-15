package com.sample.nennos

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

@SuppressLint("Registered")
open class KodeinActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by lazy { (applicationContext as KodeinAware).kodein }
}