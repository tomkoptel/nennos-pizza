package com.sample.nennos.domain

import io.reactivex.Single

interface Repo<T> {
    fun getAll(): Single<LookupOperation<List<T>>>

    fun findById(pizzaId: String): Single<LookupOperation<T>>
}

class DiskNetworkStore<T>(private val diskStore: Store<T>, private val netStore: Store<T>) : Repo<T> {
    override fun findById(pizzaId: String): Single<LookupOperation<T>> {
        return diskStore.findById(pizzaId)
    }

    override fun getAll(): Single<LookupOperation<List<T>>> {
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