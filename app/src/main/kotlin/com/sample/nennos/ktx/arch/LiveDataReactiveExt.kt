package com.sample.nennos.ktx.arch

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import org.reactivestreams.Publisher

/**
 * Converts RxJava to LiveData API.
 */
fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this)

/**
 * Converts LiveData to RxJava API.
 */
fun <T> LiveData<T>.toPublisher(lifecycleOwner: LifecycleOwner) = LiveDataReactiveStreams.toPublisher(lifecycleOwner, this)