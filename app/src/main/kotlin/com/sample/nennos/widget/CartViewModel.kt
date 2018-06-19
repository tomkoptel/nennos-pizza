package com.sample.nennos.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.nennos.domain.*
import com.sample.nennos.ktx.arch.ActionLiveData
import com.sample.nennos.rx.AppSchedulers
import com.sample.nennos.rx.fromIOToUI
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class CartViewModel(
        private val cartRepo: CartRepo,
        private val schedulers: AppSchedulers
) : ViewModel() {
    private val disposables = CompositeDisposable()
    private val savedToCartPublisher = ActionLiveData<Item>()
    private val checkOutResult = MutableLiveData<CheckOutResult>()
    private val cartUpdates = MutableLiveData<Cart>()

    init {
        cartRepo.getRecentCart().fromIOToUI(schedulers).subscribeBy(
                onNext = { cartUpdates.value = it },
                onError = Timber::e
        ).addTo(disposables)
    }

    val cartObservable: LiveData<Cart> = cartUpdates

    val onAddToCart: LiveData<Item> = savedToCartPublisher

    val onCheckOutResult: LiveData<CheckOutResult> = checkOutResult

    fun addToCart(item: Pizza) {
        val action = cartRepo.updateOrCreateCart(item).cast(Item::class.java)
        updateItem(action)
    }

    fun addToCart(item: Drink) {
        val action = cartRepo.updateOrCreateCart(item).cast(Item::class.java)
        updateItem(action)
    }

    fun removeFromCart(item: Pizza) {
        val action = cartRepo.removeItemFromCart(item).cast(Item::class.java)
        removeItem(action)
    }

    fun removeFromCart(item: Drink) {
        val action = cartRepo.removeItemFromCart(item).cast(Item::class.java)
        removeItem(action)
    }

    private fun updateItem(action: Single<Item>) {
        action.fromIOToUI(schedulers)
                .subscribeBy(
                        onSuccess = { savedToCartPublisher.value = it },
                        onError = Timber::e
                ).addTo(disposables)
    }

    private fun removeItem(action: Single<Item>) {
        action.fromIOToUI(schedulers)
                .subscribeBy(
                        onSuccess = {},
                        onError = Timber::e
                ).addTo(disposables)
    }

    fun checkOut(cart: Cart) {
        cartRepo.checkOut(cart)
                .fromIOToUI(schedulers)
                .subscribeBy(onError = {
                    checkOutResult.value = CheckOutResult.Error(it)
                }, onComplete = {
                    checkOutResult.value = CheckOutResult.Success
                })
    }

    override fun onCleared() {
        disposables.clear()
    }
}

sealed class CheckOutResult {
    object Success : CheckOutResult()
    class Error(val data: Throwable) : CheckOutResult()
}