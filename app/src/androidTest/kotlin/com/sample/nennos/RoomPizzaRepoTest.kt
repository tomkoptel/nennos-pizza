package com.sample.nennos

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.sample.nennos.data.NennoDataBase
import com.sample.nennos.data.RoomPizzaRepo
import com.sample.nennos.domain.Ingredient
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Pizza
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

    @Test
    fun test_insert_all() {
        val chiliPizza = Pizza(
                name = "Chili",
                imageUrl = "any",
                ingredients = listOf(
                        Ingredient(
                                name = "Chili Pepper",
                                price = 1.0
                        ),
                        Ingredient(
                                name = "Salami",
                                price = 2.0
                        )
                )
        )
        val sweetPizza = Pizza(
                name = "Sweet",
                imageUrl = "any",
                ingredients = listOf(
                        Ingredient(
                                name = "Tofu",
                                price = 1.0
                        ),
                        Ingredient(
                                name = "Chocolate",
                                price = 2.0
                        )
                )
        )
        val pizzas = listOf(chiliPizza, sweetPizza)
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