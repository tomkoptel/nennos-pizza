package com.sample.nennos.domain

import io.reactivex.Single

interface PizzaRepo {
    fun getAll(): Single<LookupOperation<List<Pizza>>>
}

class PizzaRepoImpl(private val diskStore: PizzaStore, private val netStore: PizzaStore) : PizzaRepo {
    override fun getAll(): Single<LookupOperation<List<Pizza>>> {
        val fromDisk = diskStore.getAll().map {
            when (it) {
                is LookupOperation.Error -> LookupOperation.Success(emptyList())
                else -> it
            }
        }
        val fromNet = netStore.getAll()
                .doOnSuccess {
                    if (it is LookupOperation.Success && !it.data.isEmpty()) {
                        diskStore.insertAll(it.data).subscribe()
                    }
                }

        return fromDisk.concatWith(fromNet)
                .filter {
                    when (it) {
                        is LookupOperation.Success -> !it.data.isEmpty()
                        else -> true
                    }
                }
                .first(LookupOperation.Success(emptyList()))
    }
}

sealed class LookupOperation<out R> {
    class Success<out R>(val data: R) : LookupOperation<R>()
    class Error<out R>(val error: Throwable) : LookupOperation<R>()
}