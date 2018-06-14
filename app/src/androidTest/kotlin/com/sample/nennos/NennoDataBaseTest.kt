package com.sample.nennos

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.sample.nennos.data.NennoDataBase
import com.sample.nennos.data.PizzaEntry
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContainAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NennoDataBaseTest {
    private lateinit var database: NennoDataBase
    private val pizzaDao by lazy { database.pizzaDao() }

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                NennoDataBase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_insert_of_pizza() {
        val noPizzas = pizzaDao.getPizzas()
        noPizzas.shouldBeEmpty()

        val pizza1 = PizzaEntry(name = "Pizza 1")
        val pizza2 = PizzaEntry(name = "Pizza 2")
        pizzaDao.insertAll(listOf(pizza1, pizza2))

        val twoPizzas = pizzaDao.getPizzas()
        twoPizzas shouldContainAll listOf(pizza1, pizza2)
    }
}