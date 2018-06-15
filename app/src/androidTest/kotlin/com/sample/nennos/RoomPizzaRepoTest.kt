package com.sample.nennos

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.sample.nennos.domain.Ingredient
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Pizza
import com.sample.nennos.persistence.NennoDataBase
import com.sample.nennos.persistence.RoomPizzaRepo
import io.reactivex.Single
import org.amshove.kluent.shouldContainAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RoomPizzaRepoTest {
    private lateinit var database: NennoDataBase
    private lateinit var roomRepo: RoomPizzaRepo

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                NennoDataBase::class.java)
                .allowMainThreadQueries()
                .build()
        roomRepo = RoomPizzaRepo { Single.just(database) }
    }

    @After
    fun tearDown() {
        database.close()
    }

    // TODO: Fix me
    @Test
    fun test_insert_all() {
        val pepper = Ingredient(name = "Chili Pepper", price = 5.0)
        val cheese = Ingredient(name = "Cheese", price = 2.0)
        val ananas = Ingredient(name = "Ananas", price = 3.0)
        val chiliPizza = Pizza(
                name = "Chili",
                imageUrl = "any",
                ingredients = listOf(pepper, cheese)
        )
        val hawaiiPizza = Pizza(
                name = "Hawaii",
                imageUrl = "any",
                ingredients = listOf(ananas, cheese)
        )

        val pizzas = listOf(chiliPizza, hawaiiPizza)
        roomRepo.insertAll(pizzas).blockingAwait(2, TimeUnit.SECONDS)

        val pizzasFromDB = roomRepo.getAll().blockingGet()
        when (pizzasFromDB) {
            is LookupOperation.Success -> {
                pizzasFromDB.data shouldContainAll pizzas
            }
            is LookupOperation.Error -> {
                throw pizzasFromDB.error
            }
        }
    }
}