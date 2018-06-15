package com.sample.nennos

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.kcontext
import org.kodein.di.generic.provider

abstract class KodeinActivity : AppCompatActivity(), KodeinAware {
    override val kodeinContext: KodeinContext<*> = kcontext(this)

    private val _parentKodein by closestKodein()

    override val kodein: Kodein = Kodein.lazy {
        extend(_parentKodein)
        bind<FragmentActivity>() with provider { this@KodeinActivity }
        import(activityModule())
    }

    abstract fun activityModule(): Kodein.Module
}