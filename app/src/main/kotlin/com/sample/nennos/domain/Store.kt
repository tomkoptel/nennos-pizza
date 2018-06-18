package com.sample.nennos.domain

import io.reactivex.Completable
import io.reactivex.Single

interface Store<T> {
    fun insertAll(entities: List<T>): Completable

    fun getAll(): Single<LookupOperation<List<T>>>

    fun findById(entityId: String): Single<LookupOperation<T>>

    enum class Type {
        DISK, NETWORK;
    }
}