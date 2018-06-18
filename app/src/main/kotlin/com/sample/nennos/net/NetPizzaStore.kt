package com.sample.nennos.net

import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Pizza
import com.sample.nennos.domain.Store
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles

class NetPizzaStore(private val apiService: ApiService) : Store<Pizza> {
    override fun findById(entityId: String): Single<LookupOperation<Pizza>> {
        return Single.never()
    }

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
                    val ingredientsData = ingredientsLookup.data
                    val ingredients = ingredientsData.map { it.id to it }.toMap()

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

    override fun insertAll(entities: List<Pizza>): Completable = Completable.complete()
}