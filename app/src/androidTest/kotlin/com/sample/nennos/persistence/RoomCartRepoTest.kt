package com.sample.nennos.persistence

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.sample.nennos.domain.Cart
import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.Ingredient
import com.sample.nennos.domain.Pizza
import io.reactivex.Single
import org.amshove.kluent.shouldContainAll
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

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                NennoDataBase::class.java)
                .allowMainThreadQueries()
                .build()
        roomStore = RoomCartRepo { Single.just(database) }
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

            val lookupPizza1 = lookupCart.pizzas.find { it.id == pizza1.id }!!
            lookupPizza1.name shouldEqual pizza1.name
            lookupPizza1.basePrice shouldEqual pizza1.basePrice
            lookupPizza1.ingredients shouldContainAll listOf(ingredient1, ingredient2)

            val lookupPizza2 = lookupCart.pizzas.find { it.id == pizza2.id }!!
            lookupPizza2.name shouldEqual pizza2.name
            lookupPizza2.basePrice shouldEqual pizza2.basePrice
            lookupPizza2.ingredients shouldContainAll listOf(ingredient1, ingredient2, ingredient3)
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
            lookupCart.drinks shouldContainAll listOf(drink1, drink2)

            val lookupDrink1 = lookupCart.drinks.find { it.id == drink1.id }!!
            lookupDrink1.name shouldEqual drink1.name
            lookupDrink1.price shouldEqual drink1.price
            lookupDrink1.remoteId shouldEqual drink1.remoteId

            val lookupDrink2 = lookupCart.drinks.find { it.id == drink2.id }!!
            lookupDrink2.name shouldEqual drink2.name
            lookupDrink2.price shouldEqual drink2.price
            lookupDrink2.remoteId shouldEqual drink2.remoteId
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