package com.sample.nennos.net

import com.sample.nennos.domain.LookupOperation
import retrofit2.adapter.rxjava2.Result

fun <R> Result<R>.toLookup(): LookupOperation<R> {
    return if (isError) {
        LookupOperation.Error(error()!!)
    } else {
        val response = response()!!
        if (response.isSuccessful) {
            LookupOperation.Success(response.body()!!)
        } else {
            LookupOperation.Error(HttpServerError("Server yields HTTP code ${response.code()}"))
        }
    }
}