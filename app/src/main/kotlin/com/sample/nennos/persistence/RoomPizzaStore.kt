package com.sample.nennos.persistence

import com.sample.nennos.domain.Ingredient
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Pizza
import com.sample.nennos.domain.PizzaStore
import io.reactivex.Completable
import io.reactivex.Single

class RoomPizzaStore(private val dbProvider: () -> Single<NennoDataBase>) : PizzaStore {
    constructor(factory: NennoDataBase.Factory) : this({
        factory.getDatabase()
    })

    override fun insertAll(pizzas: List<Pizza>): Completable {
        return dbProvider().map {
            val pizzaWithIngredients = pizzas.map {
                val pizzaEntity = it.toDataObject()
                val ingredientEntities = it.ingredients.map(Ingredient::toDataObject)

                pizzaEntity to ingredientEntities
            }.toMap()

            val pizzaEntities = pizzaWithIngredients.keys
            it.pizzaDao().insertAll(pizzaEntities)

            val ingredientEntities = pizzaWithIngredients.values.flatten().toHashSet()
            it.ingredientDao().insertAll(ingredientEntities)

            val joinEntities = PizzaIngredientEntity.fromMapping(pizzaWithIngredients)
            it.pizzaIngredientJoinDao().insertAll(joinEntities)
        }.ignoreElement()
    }

    override fun getAll(): Single<LookupOperation<List<Pizza>>> =
            dbProvider().map {
                val pizzaDao = it.pizzaDao()
                val pizzaIngredientJoinDao = it.pizzaIngredientJoinDao()

                val pizzas = pizzaDao.getPizzas()
                        .map { pizzaEntity ->
                            val ingredients = pizzaIngredientJoinDao.getIngredientsForPizza(pizzaEntity.uid)
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