package com.sample.nennos.data

import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Pizza
import com.sample.nennos.domain.PizzaRepo
import io.reactivex.Completable
import io.reactivex.Single

class RoomPizzaRepo(
        private val dbProvider: () -> Single<NennoDataBase>
) : PizzaRepo {
    constructor(factory: NennoDataBase.Factory) : this({
        factory.getDatabase()
    })

    override fun insertAll(pizzas: List<Pizza>): Completable {
        return dbProvider().map {
            val pizzaDao = it.pizzaDao()
            val ingredientDao = it.ingredientDao()

            it.beginTransaction()
            try {
                pizzas.forEach {
                    val pizza = it.toDataObject()
                    val ingredients = it.ingredients.map { it.toDataObject(pizza) }

                    pizzaDao.insert(pizza)
                    ingredientDao.insertAll(ingredients)
                }

                it.setTransactionSuccessful()
            } finally {
                it.endTransaction()
            }
        }.ignoreElement()
    }

    override fun getAll(): Single<LookupOperation<List<Pizza>>> =
            dbProvider().map {
                val pizzaDao = it.pizzaDao()
                val ingredientDao = it.ingredientDao()
                val pizzas = pizzaDao.getPizzas()
                        .map { pizzaEntity ->
                            val ingredients = ingredientDao.findIngredientsForPizza(pizzaEntity.uid)
                                    .map(IngredientEntity::toDomainObject)
                            Pizza(id = pizzaEntity.uid,
                                    name = pizzaEntity.name,
                                    imageUrl = pizzaEntity.imageUrl,
                                    ingredients = ingredients
                            )
                        }
                LookupOperation.Success(pizzas) as LookupOperation<List<Pizza>>
            }.onErrorReturn { LookupOperation.Error(it) }
}