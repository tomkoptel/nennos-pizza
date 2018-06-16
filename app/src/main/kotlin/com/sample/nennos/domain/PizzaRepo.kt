package com.sample.nennos.domain

import io.reactivex.Single
import io.reactivex.disposables.Disposables

interface PizzaRepo {
    fun getAll(): Single<LookupOperation<List<Pizza>>>
}

class PizzaRepoImpl(private val diskStore: PizzaStore, private val netStore: PizzaStore) : PizzaRepo {
    private var disposable = Disposables.empty()

    override fun getAll(): Single<LookupOperation<List<Pizza>>> {
        val fromDisk = diskStore.getAll()
        val fromNet = netStore.getAll()
                .doOnSuccess {
                    if (it is LookupOperation.Success) {
                        disposable = diskStore.insertAll(it.data).subscribe()
                    }
                }
                .doOnDispose { disposable.dispose() }

        return fromDisk.concatWith(fromNet).firstOrError()
    }
}

sealed class LookupOperation<out R> {
    class Success<out R>(val data: R) : LookupOperation<R>()
    class Error<out R>(val error: Throwable) : LookupOperation<R>()
}