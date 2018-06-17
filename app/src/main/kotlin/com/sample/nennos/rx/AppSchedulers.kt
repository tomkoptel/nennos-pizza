package com.sample.nennos.rx

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers as PlatformSchedulers

interface AppSchedulers {
    fun io() : Scheduler
    fun ui() : Scheduler
    fun computation() : Scheduler
}

fun <T> Flowable<T>.fromComputationToUI(provider: AppSchedulers): Flowable<T> = compose {
    it.subscribeOn(provider.computation()).observeOn(provider.ui())
}

fun <T> Flowable<T>.fromIOToUI(provider: AppSchedulers): Flowable<T> = compose {
    it.subscribeOn(provider.io()).observeOn(provider.ui())
}

fun <T> Single<T>.fromIOToUI(provider: AppSchedulers) : Single<T> = compose {
    it.subscribeOn(provider.io()).observeOn(provider.ui())
}

fun Completable.fromIOToUI(provider: AppSchedulers): Completable = compose {
    it.subscribeOn(provider.io()).observeOn(provider.ui())
}

object AppSchedulerProvider : AppSchedulers {
    override fun io(): Scheduler = PlatformSchedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = PlatformSchedulers.computation()
}