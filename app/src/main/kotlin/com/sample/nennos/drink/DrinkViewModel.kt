package com.sample.nennos.drink

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.LookupOperation

class DrinkViewModel : ViewModel() {
    val onDrinks: LiveData<LookupOperation<List<Drink>>> = TODO()

    val onDrinkAdded: LiveData<Drink> = TODO()

    fun addDrink(drink: Drink) {
    }
}