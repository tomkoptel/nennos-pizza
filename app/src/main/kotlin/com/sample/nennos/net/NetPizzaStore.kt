package com.sample.nennos.net

import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Pizza
import com.sample.nennos.domain.PizzaStore
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles

class NetPizzaStore(private val apiService: ApiService) : PizzaStore {
    override fun getAll(): Single<LookupOperation<List<Pizza>>> {
        return Singles.zip(apiService.getPizzas(), apiService.getIngredients()) { pizzaResponse, ingredientsResponse ->
            val pizzaLookup = pizzaResponse.toLookup()
            val ingredientsLookup = ingredientsResponse.toLookup()

            when {
                pizzaLookup is LookupOperation.Error -> {
                    LookupOperation.Error<List<Pizza>>(pizzaLookup.error)
                }
                ingredientsLookup is LookupOperation.Error -> {
                    LookupOperation.Error<List<Pizza>>(ingredientsLookup.error)
                }
                pizzaLookup is LookupOperation.Success && ingredientsLookup is LookupOperation.Success -> {
                    val (pizzas, basePrice) = pizzaLookup.data
                    val ingredients = ingredientsLookup.data.map { it.id to it }.toMap()

                    val result = pizzas.map {
                        val filteredIngredients = it.ingredients.map { ingredients.getValue(it).toDomainObject() }
                        Pizza(name = it.name, imageUrl = it.imageUrl, basePrice = basePrice, ingredients = filteredIngredients)
                    }
                    LookupOperation.Success<List<Pizza>>(result)
                }
                else -> throw IllegalStateException("Exhaustive condition! Should not happen!")
            }
        }
    }

    override fun insertAll(pizzas: List<Pizza>): Completable = Completable.complete()
}