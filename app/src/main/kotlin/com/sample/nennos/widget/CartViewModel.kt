package com.sample.nennos.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sample.nennos.domain.Cart
import com.sample.nennos.domain.CartRepo
import com.sample.nennos.domain.Item
import com.sample.nennos.ktx.arch.ActionLiveData
import com.sample.nennos.ktx.arch.toLiveData
import com.sample.nennos.rx.AppSchedulers
import com.sample.nennos.rx.fromIOToUI
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class CartViewModel(private val cartRepo: CartRepo, private val schedulers: AppSchedulers) : ViewModel() {
    private val disposables = CompositeDisposable()
    private val savedToCartPublisher = ActionLiveData<Item>()

    val cartObservable: LiveData<Cart> by lazy(LazyThreadSafetyMode.NONE) {
        cartRepo.getRecentCart().fromIOToUI(schedulers).toLiveData()
    }

    val onAddToCart: LiveData<Item> = savedToCartPublisher

    fun addToCart(item: Item) {
        cartRepo.updateOrCreateCart(item)
                .fromIOToUI(schedulers)
                .subscribeBy(
                        onSuccess = { savedToCartPublisher.value = it },
                        onError = Timber::e
                ).addTo(disposables)
    }

    fun removeFromCart(item: Item) {
        cartRepo.removeItemFromCart(item)
                .fromIOToUI(schedulers)
                .subscribeBy(
                        onSuccess = {},
                        onError = Timber::e
                ).addTo(disposables)
    }

    override fun onCleared() {
        disposables.clear()
    }
}