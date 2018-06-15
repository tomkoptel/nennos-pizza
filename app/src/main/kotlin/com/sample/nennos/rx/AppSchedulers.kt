package com.sample.nennos.rx

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import org.reactivestreams.Publisher
import io.reactivex.schedulers.Schedulers as PlatformSchedulers

interface AppSchedulers {
    fun io() : Scheduler
    fun ui() : Scheduler
    fun computation() : Scheduler
}

fun <T> Single<T>.fromIOToUI(provider: AppSchedulers) : Single<T> = compose {
    it.subscribeOn(provider.io()).observeOn(provider.ui())
}

object AppSchedulerProvider : AppSchedulers {
    override fun io(): Scheduler = PlatformSchedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = PlatformSchedulers.computation()
}