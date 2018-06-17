package com.sample.nennos.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.PizzaRepo
import com.sample.nennos.ktx.toChoice
import com.sample.nennos.rx.AppSchedulers
import com.sample.nennos.rx.fromComputationToUI
import com.sample.nennos.rx.fromIOToUI
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class PizzaDetailViewModel(
        private val pizzaRepo: PizzaRepo,
        private val schedulers: AppSchedulers
) : ViewModel() {
    private val onIngredientChecked = PublishProcessor.create<IngredientChoice>()
    private val disposables = CompositeDisposable()

    private val mutablePizza = MutableLiveData<LookupOperation<PizzaChoice>>()

    val onPizzaChange: LiveData<LookupOperation<PizzaChoice>> = mutablePizza

    init {
        onIngredientChecked.switchMap { choice ->
            mutablePizza.value?.let { operation ->
                when (operation) {
                    is LookupOperation.Success -> {
                        val pizzaChoice = operation.data.copyWithNewIngredient(choice)
                        if (pizzaChoice == null) {
                            Flowable.empty()
                        } else {
                            LookupOperation.Success(pizzaChoice).let { Flowable.just(it) }
                        }
                    }
                    is LookupOperation.Error -> {
                        LookupOperation.asError<PizzaChoice>(operation).let { Flowable.just(it) }
                    }
                }
            }
        }
                .fromComputationToUI(schedulers)
                .subscribeBy(onNext = ::emitNewChoice, onError = Timber::e)
                .addTo(disposables)
    }

    fun loadPizza(pizzaId: String) {
        pizzaRepo.findById(pizzaId)
                .fromIOToUI(schedulers)
                .map { operation ->
                    when (operation) {
                        is LookupOperation.Success -> {
                            operation.data.toChoice().let {
                                LookupOperation.Success(it)
                            }
                        }
                        is LookupOperation.Error -> {
                            LookupOperation.asError<PizzaChoice>(operation)
                        }
                    }
                }
                .subscribeBy(onSuccess = ::emitNewChoice, onError = Timber::e)
                .addTo(disposables)
    }

    fun recalculatePrice(choice: IngredientChoice) {
        onIngredientChecked.onNext(choice)
    }

    override fun onCleared() {
        disposables.clear()
    }

    private fun emitNewChoice(operation: LookupOperation<PizzaChoice>) {
        mutablePizza.value = operation
    }
}