package com.sample.nennos.domain

sealed class LookupOperation<out R> {
    companion object {
        fun <R> asError(operation: LookupOperation.Error<Any>) = LookupOperation.Error<R>(operation.error)
    }

    class Success<out R>(val data: R) : LookupOperation<R>()
    class Error<out R>(val error: Throwable) : LookupOperation<R>()
}