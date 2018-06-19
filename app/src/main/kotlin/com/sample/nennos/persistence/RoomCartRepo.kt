package com.sample.nennos.persistence

import androidx.room.RxRoom
import com.sample.nennos.domain.Cart
import com.sample.nennos.domain.CartRepo
import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.Pizza
import com.sample.nennos.net.ApiService
import com.sample.nennos.net.checkOut
import com.sample.nennos.net.toNetDataObject
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.threeten.bp.OffsetDateTime

class RoomCartRepo(private val apiService: ApiService, private val dbProvider: () -> Single<NennoDataBase>) : CartRepo {
    constructor(apiService: ApiService, factory: NennoDataBase.Factory) : this(apiService, {
        factory.getDatabase()
    })

    override fun updateOrCreateCart(item: Pizza): Single<Pizza> {
        return dbProvider().flatMap { database ->
            currentCart(database) { cart ->
                val cartDao = database.cartDao()
                val cartId = yieldOrCreateNewCart(cartDao, cart)
                val pizzaId = item.id

                val ingredients = item.ingredients.joinToString { it.id }
                val cartPizzaEntity = CartPizzaEntity(cartId = cartId, pizzaId = pizzaId, ingredients = ingredients)
                cartDao.insertPizza(cartPizzaEntity)

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
                database.cartDao().removePizza(cartId = cart.id, pizzaId = item.id)
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
            RxRoom.createFlowable(db, "CartPizza", "CartDrink")
                    .flatMap {
                        db.cartDao().getRecentCarts().map { mapCartEntity(db, it) }
                    }
                    .distinctUntilChanged()
        }
    }

    override fun checkOut(cart: Cart): Completable {
        val updateInDb = dbProvider().map {
            val currentCart = cart.toDataObject()
            val newCart = currentCart.copy(checkedOutAt = OffsetDateTime.now())

            it.cartDao().updateCart(newCart)
            Unit
        }.ignoreElement()

        return Single.fromCallable { cart.toNetDataObject() }
                .flatMapCompletable { apiService.checkOut(it) }
                .concatWith(updateInDb)
    }

    private fun mapCartEntity(dataBase: NennoDataBase, recentCarts: List<CartEntity>): Cart {
        val cartDao = dataBase.cartDao()
        val ingredientDao = dataBase.ingredientDao()
        val pizzaDao = dataBase.pizzaDao()

        return if (recentCarts.isEmpty()) {
            Cart.NULL
        } else {
            val cart = recentCarts.last()
            val cartId = cart.uid

            val cartPizzaEntities = cartDao.getCartPizzaEntitiesByCartId(cartId = cartId)
            val pizzas = cartPizzaEntities.map {
                val pizza = pizzaDao.findPizzaById(it.pizzaId)
                val ids = it.ingredients.split(",").map { it.trim() }
                val ingredientEntities = ingredientDao.getIngredients(ids)
                val ingredients = ingredientEntities.map(IngredientEntity::toDomainObject)
                pizza.toDomainObject(ingredients).copy(id = it.pid)
            }

            val drinkCartEntitiesByCartId = cartDao.getCartDrinkEntitiesByCartId(cartId)
            val drinkEntitiesByCartId = cartDao.getDrinksByCartId(cartId).map { it.uid to it }.toMap()
            val drinks = drinkCartEntitiesByCartId.map {
                drinkEntitiesByCartId.getValue(it.drinkId).toDomainObject().copy(id = it.pid)
            }

            cart.toDomainObject(pizzas, drinks)
        }
    }

    private fun yieldOrCreateNewCart(cartDao: CartDao, cart: Cart): String {
        return if (cart == Cart.NULL) {
            val newCart = CartEntity()
            cartDao.insertPizza(newCart)
            newCart.uid
        } else {
            cart.id
        }
    }
}