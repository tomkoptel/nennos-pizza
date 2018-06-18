package com.sample.nennos.persistence

import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Store
import io.reactivex.Completable
import io.reactivex.Single

class RoomDrinkStore(private val dbProvider: () -> Single<NennoDataBase>) : Store<Drink> {
    constructor(factory: NennoDataBase.Factory) : this({
        factory.getDatabase()
    })

    override fun insertAll(entities: List<Drink>): Completable = dbProvider().map {
        it.drinkDao().insertAll(entities.map(Drink::toDataEntity))
    }.ignoreElement()

    override fun getAll(): Single<LookupOperation<List<Drink>>> =
            dbProvider().map { db ->
                val drinkDao = db.drinkDao()
                val drinks = drinkDao.getDrinks().map(DrinkEntity::toDomainObject)

                LookupOperation.Success(drinks) as LookupOperation<List<Drink>>
            }.onErrorReturn { LookupOperation.Error(it) }

    override fun findById(entityId: String) = dbProvider().map {
        it.drinkDao().findById(entityId).toDomainObject().let {
            LookupOperation.Success<Drink>(it) as LookupOperation<Drink>
        }
    }.onErrorReturn { LookupOperation.Error(it) }
}