package com.sample.nennos.pizzas

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Pizza
import com.sample.nennos.domain.PizzaRepo
import com.sample.nennos.ktx.arch.toLiveData
import com.sample.nennos.rx.AppSchedulers
import com.sample.nennos.rx.fromIOToUI

class PizzaListViewModel(
        private val pizzaRepo: PizzaRepo,
        private val schedulers: AppSchedulers
) : ViewModel() {
    val pizzaList: LiveData<LookupOperation<List<Pizza>>> by lazy {
        pizzaRepo.getAll()
                .fromIOToUI(schedulers)
                .toFlowable()
                .toLiveData()
    }
}