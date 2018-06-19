package com.sample.nennos.persistence

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.sample.nennos.domain.Cart
import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.Ingredient
import com.sample.nennos.domain.Pizza
import com.sample.nennos.net.ApiService
import io.mockk.mockk
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.threeten.bp.OffsetDateTime

class RoomCartRepoTest {
    private lateinit var database: NennoDataBase
    private lateinit var roomStore: RoomCartRepo
    private val cartDao by lazy { database.cartDao() }
    private val pizzaDao by lazy { database.pizzaDao() }
    private val ingredientDao by lazy { database.ingredientDao() }
    private val drinkDao by lazy { database.drinkDao() }

    private val ingredient1 = Ingredient(name = "Ingredient 1", price = 1.0, remoteId = 1)
    private val ingredient2 = Ingredient(name = "Ingredient 2", price = 1.0, remoteId = 2)
    private val ingredient3 = Ingredient(name = "Ingredient 3", price = 1.0, remoteId = 3)

    private val pizza1 = Pizza(name = "Pizza 1", basePrice = 10.0, ingredients = listOf(ingredient1, ingredient2))
    private val pizza2 = Pizza(name = "Pizza 2", basePrice = 12.0, ingredients = listOf(ingredient1, ingredient2, ingredient3))

    private val drink1 = Drink(name = "Drink 1", price = 1.0, remoteId = 1)
    private val drink2 = Drink(name = "Drink 2", price = 1.0, remoteId = 2)

    private val apiService = mockk<ApiService>()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                NennoDataBase::class.java)
                .allowMainThreadQueries()
                .build()
        roomStore = RoomCartRepo(apiService) { Single.just(database) }
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_updateOrCreateCart_with_pizza() {
        pizzaDao.insertAll(listOf(pizza1.toDataObject(), pizza2.toDataObject()))
        ingredientDao.insertAll(listOf(ingredient1.toDataObject(), ingredient2.toDataObject(), ingredient3.toDataObject()))

        insertPizza(pizza1)
        insertPizza(pizza2)

        roomStore.getRecentCart().test().apply {
            assertNoErrors()
            awaitCount(1)

            val lookupCart = values().first()
            lookupCart.pizzas.size shouldEqual 2
        }
    }

    @Test
    fun test_updateOrCreateCart_with_drink() {
        drinkDao.insertAll(listOf(drink1.toDataEntity(), drink2.toDataEntity()))

        insertDrink(drink1)
        insertDrink(drink2)

        roomStore.getRecentCart().test().apply {
            assertNoErrors()
            awaitCount(1)

            val lookupCart = values().first()
            lookupCart.drinks.size shouldEqual 2
        }
    }

    @Test
    fun test_getRecentCart() {
        val completedCart = Cart(
                createdAt = OffsetDateTime.now().minusHours(1),
                checkedOutAt = OffsetDateTime.now().plusHours(1)
        )
        val currentCart1 = Cart(createdAt = OffsetDateTime.now().minusHours(1))
        val currentCart2 = Cart(createdAt = OffsetDateTime.now())

        cartDao.insertPizza(completedCart.toDataObject())
        cartDao.insertPizza(currentCart1.toDataObject())
        cartDao.insertPizza(currentCart2.toDataObject())

        roomStore.getRecentCart().test().apply {
            assertNoErrors()
            awaitCount(1)

            val lookupCart = values().first()
            lookupCart shouldEqual currentCart2
        }
    }

    private fun insertPizza(pizza: Pizza) {
        roomStore.updateOrCreateCart(pizza).test().apply {
            assertNoErrors()
            awaitCount(1)
        }
    }

    private fun insertDrink(drink: Drink) {
        roomStore.updateOrCreateCart(drink).test().apply {
            assertNoErrors()
            awaitCount(1)
        }
    }
}