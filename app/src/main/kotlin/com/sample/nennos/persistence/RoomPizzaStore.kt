package com.sample.nennos.persistence

import com.sample.nennos.domain.Ingredient
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Pizza
import com.sample.nennos.domain.Store
import io.reactivex.Single

class RoomPizzaStore(private val dbProvider: () -> Single<NennoDataBase>) : Store<Pizza> {
    constructor(factory: NennoDataBase.Factory) : this({
        factory.getDatabase()
    })

    override fun findById(pizzaId: String) = dbProvider().map {
        val pizzaDao = it.pizzaDao()

        val pizzaEntity = pizzaDao.findPizzaById(pizzaId)
        val pizza = it.mapPizzaEntity(pizzaEntity)

        LookupOperation.Success(pizza) as LookupOperation<Pizza>
    }.onErrorReturn { LookupOperation.Error(it) }


    override fun insertAll(pizzas: List<Pizza>) = dbProvider().map {
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

    override fun getAll(): Single<LookupOperation<List<Pizza>>> =
            dbProvider().map { db ->
                val pizzaDao = db.pizzaDao()
                val pizzas = pizzaDao.getPizzas().map { db.mapPizzaEntity(it) }

                LookupOperation.Success(pizzas) as LookupOperation<List<Pizza>>
            }.onErrorReturn { LookupOperation.Error(it) }

    private fun NennoDataBase.mapPizzaEntity(pizzaEntity: PizzaEntity): Pizza {
        val ingredients = pizzaIngredientJoinDao()
                .getIngredientsForPizza(pizzaEntity.uid)
                .map(IngredientEntity::toDomainObject)
        return pizzaEntity.toDomainObject(ingredients)
    }
}