package com.sample.nennos.ktx.arch

import androidx.lifecycle.*


/**
 * Alias api that replaces [Observer] with lambda. Produces cleaner API.
 * ```
 * liveData.observe(lifecycleOwner) {}
 * ```
 * Instead of:
 * ```
 * liveData.observe(lifecycleOwner, Observer<Response> {})
 * ```
 */
fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T?) -> Unit) {
    this.observe(owner, Observer<T> { observer(it) })
}

/**
 * The same as [observe], but never yields null values.
 */
fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner) { it?.let(observer) }
}

/**
 * Syntactic sugar around [Transformations.map] to transform [LiveData] objects in chain.
 * ```
 * liveData.map {}
 * ```
 * Instead of:
 * ```
 * Transformations.map(liveData) {}
 * ```
 */
fun <T, R> LiveData<T>.map(transformation: (T) -> R): LiveData<R> {
    return Transformations.map(this, transformation)
}

/**
 * Syntactic sugar around [Transformations.switchMap] to transform [LiveData] objects in chain.
 * ```
 * liveData.switchMap {}
 * ```
 * Instead of:
 * ```
 * Transformations.switchMap(liveData) {}
 * ```
 */
fun <T, R> LiveData<T>.switchMap(transformation: (T) -> LiveData<R>): LiveData<R> {
    return Transformations.switchMap(this, transformation)
}

/**
 * Allows to combine results from two [LiveData] sources. Useful when you need to combine results from 2 sources of data.
 * ```
 * liveData.combineWith {}
 * ```
 */
fun <A, B, C> LiveData<A>.combineWith(other: LiveData<B>, zipFunc: (A, B) -> C): LiveData<C> {
    return CombinedLiveData<A, B, C>(this, other, zipFunc)
}

private class CombinedLiveData<A, B, C>(
        ldA: LiveData<A>,
        ldB: LiveData<B>,
        private val zipFunc: (A, B) -> C
) : MediatorLiveData<C>() {
    private var lastValueA: A? = null
    private var lastValueB: B? = null

    init {
        addSource(ldA) {
            lastValueA = it
            emitZipped()
        }
        addSource(ldB) {
            lastValueB = it
            emitZipped()
        }
    }

    private fun emitZipped() {
        val valueA = lastValueA
        val valueB = lastValueB
        if (valueA != null && valueB != null) {
            value = zipFunc(valueA, valueB)
        }
    }
}