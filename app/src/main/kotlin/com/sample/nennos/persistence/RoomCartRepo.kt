package com.sample.nennos.persistence

import com.sample.nennos.domain.Cart
import com.sample.nennos.domain.CartRepo
import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.Pizza
import io.reactivex.Flowable
import io.reactivex.Single

class RoomCartRepo(private val dbProvider: () -> Single<NennoDataBase>) : CartRepo {
    constructor(factory: NennoDataBase.Factory) : this({
        factory.getDatabase()
    })

    override fun updateOrCreateCart(item: Pizza): Single<Pizza> {
        return dbProvider().flatMap { database ->
            currentCart(database) { cart ->
                val cartDao = database.cartDao()
                val cartId = yieldOrCreateNewCart(cartDao, cart)
                val pizzaId = item.id

                val cartPizzaEntity = CartPizzaEntity(cartId = cartId, pizzaId = pizzaId)
                val cartIngredientEntity = item.ingredients.map { CartIngredientEntity(cartId = cartId, ingredientId = it.id, pizzaId = pizzaId) }
                cartDao.insertCartPizzaWithIngredients(cartPizzaEntity, cartIngredientEntity)

                item
            }
        }
    }

    override fun updateOrCreateCart(item: Drink): Single<Drink> {
        return dbProvider().flatMap { database ->
            currentCart(database) { cart ->
                val cartDao = database.cartDao()
                val cartId = yieldOrCreateNewCart(cartDao, cart)

                val cartDrinkEntity = CartDrinkEntity(cartId = cartId, drinkId = item.id)
                cartDao.insertDrink(cartDrinkEntity)

                item
            }
        }
    }

    override fun removeItemFromCart(item: Pizza): Single<Pizza> {
        return dbProvider().flatMap { database ->
            currentCart(database) { cart ->
                database.cartDao().removePizzaIngredients(cartId = cart.id, pizzaId = item.id)
                item
            }
        }
    }

    override fun removeItemFromCart(item: Drink): Single<Drink> {
        return dbProvider().flatMap { database ->
            currentCart(database) { cart ->
                database.cartDao().removeDrink(cartId = cart.id, drinkId = item.id)
                item
            }
        }
    }

    private fun <T> currentCart(db: NennoDataBase, block: (Cart) -> T): Single<T> {
        return db.cartDao().getRecentCarts().take(1)
                .map { mapCartEntity(db, it) }
                .flatMapSingle { Single.just(block.invoke(it)) }
                .singleOrError()
    }

    override fun getRecentCart(): Flowable<Cart> {
        return dbProvider().flatMapPublisher { db ->
            db.cartDao().getRecentCarts()
                    .distinctUntilChanged()
                    .map { mapCartEntity(db, it) }
        }
    }

    private fun mapCartEntity(dataBase: NennoDataBase, recentCarts: List<CartEntity>): Cart {
        val cartDao = dataBase.cartDao()

        return if (recentCarts.isEmpty()) {
            Cart.NULL
        } else {
            val cart = recentCarts.last()
            val cartId = cart.uid

            val pizzasByCartId = cartDao.getPizzasByCartId(cartId)
            val pizzas = pizzasByCartId.map {
                val ingredientEntities = cartDao.getIngredientsByPizzaAndCartId(cartId, it.uid)
                val ingredients = ingredientEntities.map(IngredientEntity::toDomainObject)
                it.toDomainObject(ingredients)
            }
            val drinksByCartId = cartDao.getDrinksByCartId(cartId)
            val drinks = drinksByCartId.map(DrinkEntity::toDomainObject)

            cart.toDomainObject(pizzas, drinks)
        }
    }

    private fun yieldOrCreateNewCart(cartDao: CartDao, cart: Cart): String {
        return if (cart == Cart.NULL) {
            val newCart = CartEntity()
            cartDao.insertCart(newCart)
            newCart.uid
        } else {
            cart.id
        }
    }
}