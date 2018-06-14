package com.sample.nennos

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.sample.nennos.data.DrinkEntity
import com.sample.nennos.data.IngredientEntity
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
    private val ingredientDao by lazy { database.ingredientDao() }
    private val drinkDao by lazy { database.drinkDao() }

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
    fun test_insert_of_pizzas() {
        val noPizzas = pizzaDao.getPizzas()
        noPizzas.shouldBeEmpty()

        val pizza1 = PizzaEntry(name = "Pizza 1")
        val pizza2 = PizzaEntry(name = "Pizza 2")
        pizzaDao.insertAll(listOf(pizza1, pizza2))

        val twoPizzas = pizzaDao.getPizzas()
        twoPizzas shouldContainAll listOf(pizza1, pizza2)
    }

    @Test
    fun test_insert_of_ingredients() {
        val noIngredients = ingredientDao.getIngredients()
        noIngredients.shouldBeEmpty()

        val pizza1 = PizzaEntry(name = "Pizza 1")
        val pizza2 = PizzaEntry(name = "Pizza 2")
        pizzaDao.insertAll(listOf(pizza1, pizza2))

        val ingredient1 = IngredientEntity(name = "Ingredient 1", price = 1.0, pizzaId = pizza1.uid)
        val ingredient2 = IngredientEntity(name = "Ingredient 2", price = 2.0, pizzaId = pizza2.uid)
        ingredientDao.insertAll(listOf(ingredient1, ingredient2))

        val twoIngredients = ingredientDao.getIngredients()
        twoIngredients shouldContainAll listOf(ingredient1, ingredient2)

        val ingredientForPizza1 = ingredientDao.findIngredientsForPizza(pizza1.uid)
        ingredientForPizza1 shouldContainAll listOf(ingredient1)

        val ingredientForPizza2 = ingredientDao.findIngredientsForPizza(pizza2.uid)
        ingredientForPizza2 shouldContainAll listOf(ingredient2)
    }

    @Test
    fun test_insert_of_drinks() {
        val noDrinks = drinkDao.getDrinks()
        noDrinks.shouldBeEmpty()

        val drink1 = DrinkEntity(name = "Drink 1", price = 1.0)
        val drink2 = DrinkEntity(name = "Drink 2", price = 2.0)
        drinkDao.insertAll(listOf(drink1, drink2))

        val twoDrinks = drinkDao.getDrinks()
        twoDrinks shouldContainAll listOf(drink1, drink2)
    }
}