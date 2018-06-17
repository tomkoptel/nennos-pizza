package com.sample.nennos

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.sample.nennos.domain.Ingredient
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Pizza
import com.sample.nennos.persistence.NennoDataBase
import com.sample.nennos.persistence.RoomPizzaStore
import io.reactivex.Single
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqual
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RoomPizzaStoreTest {
    private lateinit var database: NennoDataBase
    private lateinit var roomStore: RoomPizzaStore

    private val pepper = Ingredient(name = "Chili Pepper", price = 5.0)
    private val cheese = Ingredient(name = "Cheese", price = 2.0)
    private val ananas = Ingredient(name = "Ananas", price = 3.0)
    private val chiliPizza = Pizza(
            basePrice = 10.0,
            name = "Chili",
            imageUrl = "any1",
            ingredients = listOf(pepper, cheese)
    )
    private val hawaiiPizza = Pizza(
            basePrice = 15.0,
            name = "Hawaii",
            imageUrl = "any2",
            ingredients = listOf(ananas, cheese)
    )
    private val pizzas = listOf(chiliPizza, hawaiiPizza)

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                NennoDataBase::class.java)
                .allowMainThreadQueries()
                .build()
        roomStore = RoomPizzaStore { Single.just(database) }
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_insert_all() {
        roomStore.insertAll(pizzas).blockingAwait(2, TimeUnit.SECONDS)

        val pizzasFromDB = roomStore.getAll().blockingGet()

        pizzasFromDB.assertSuccessResult { result ->
            val firstPizza = result.first()
            firstPizza.name shouldEqual chiliPizza.name
            firstPizza.imageUrl shouldEqual chiliPizza.imageUrl
            firstPizza.basePrice shouldEqual chiliPizza.basePrice
            firstPizza.ingredients shouldContainAll listOf(pepper, cheese)

            val secondPizza = result.last()
            secondPizza.name shouldEqual hawaiiPizza.name
            secondPizza.imageUrl shouldEqual hawaiiPizza.imageUrl
            secondPizza.basePrice shouldEqual hawaiiPizza.basePrice
            secondPizza.ingredients shouldContainAll listOf(ananas, cheese)
        }
    }

    @Test
    fun test_find_pizza() {
        roomStore.insertAll(pizzas).blockingAwait(2, TimeUnit.SECONDS)

        val pizzaFromDB = roomStore.findById(hawaiiPizza.id).blockingGet()

        pizzaFromDB.assertSuccessResult { pizza ->
            pizza.name shouldEqual hawaiiPizza.name
            pizza.imageUrl shouldEqual hawaiiPizza.imageUrl
            pizza.basePrice shouldEqual hawaiiPizza.basePrice
            pizza.ingredients shouldContainAll listOf(ananas, cheese)
        }
    }

    private fun <R> LookupOperation<R>.assertSuccessResult(block: (R) -> Unit) = when (this) {
        is LookupOperation.Success -> block(this.data)
        is LookupOperation.Error -> throw error
    }
}