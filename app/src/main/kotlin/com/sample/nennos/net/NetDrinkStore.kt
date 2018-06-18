package com.sample.nennos.net

import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Store
import io.reactivex.Completable
import io.reactivex.Single

class NetDrinkStore(private val apiService: ApiService) : Store<Drink> {
    override fun insertAll(entities: List<Drink>): Completable = Completable.complete()

    override fun findById(entityId: String) = Single.never<LookupOperation<Drink>>()

    override fun getAll(): Single<LookupOperation<List<Drink>>> {
        return apiService.getDrinks().map {
            val result = it.toLookup<List<DrinkData>>()

            when (result) {
                is LookupOperation.Success -> {
                    val drinks = result.data.map(DrinkData::toDomainObject)
                    LookupOperation.Success<List<Drink>>(drinks)
                }
                is LookupOperation.Error -> LookupOperation.asError<List<Drink>>(result)
            }
        }
    }
}