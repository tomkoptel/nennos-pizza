package com.sample.nennos.drink

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Repo
import com.sample.nennos.ktx.arch.toLiveData
import com.sample.nennos.rx.AppSchedulers
import com.sample.nennos.rx.fromIOToUI

class DrinkViewModel(private val repo: Repo<Drink>,
                     private val appSchedulers: AppSchedulers) : ViewModel() {
    val onDrinks: LiveData<LookupOperation<List<Drink>>> by lazy(LazyThreadSafetyMode.NONE) {
        repo.getAll().fromIOToUI(appSchedulers).toFlowable().toLiveData()
    }
}