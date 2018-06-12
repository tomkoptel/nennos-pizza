package com.sample.nennos.kodein

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.kcontext
import org.kodein.di.generic.provider

abstract class KodeinFragment : Fragment(), KodeinAware {
    override val kodeinContext: KodeinContext<*> = kcontext(this)

    private val _parentKodein by closestKodein { activity!! }

    override val kodein: Kodein = Kodein.lazy {
        extend(_parentKodein)
        bind<FragmentActivity>() with provider { activity as FragmentActivity }
        import(activityModule())
    }

    abstract fun activityModule(): Kodein.Module
}