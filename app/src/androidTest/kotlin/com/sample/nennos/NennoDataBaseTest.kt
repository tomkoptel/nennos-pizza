package com.sample.nennos

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.sample.nennos.persistence.*
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
    private val pizzaIngredientJoinDao by lazy { database.pizzaIngredientJoinDao() }

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
    fun test_insert_of_ingredients_and_pizza() {
        val pizza1 = PizzaEntity(name = "Pizza 1")
        val pizza2 = PizzaEntity(name = "Pizza 2")
        pizzaDao.insertAll(listOf(pizza1, pizza2))

        val twoPizzas = pizzaDao.getPizzas()
        twoPizzas shouldContainAll listOf(pizza1, pizza2)

        val ingredient1 = IngredientEntity(name = "Ingredient 1", price = 1.0)
        val ingredient2 = IngredientEntity(name = "Ingredient 2", price = 2.0)
        val ingredient3 = IngredientEntity(name = "Ingredient 3", price = 3.0)
        ingredientDao.insertAll(listOf(ingredient1, ingredient2, ingredient3))

        val threeIngredients = ingredientDao.getIngredients()
        threeIngredients shouldContainAll listOf(ingredient1, ingredient2, ingredient3)

        val relations = mapOf(
                pizza1 to listOf(ingredient1, ingredient2),
                pizza2 to listOf(ingredient1, ingredient2, ingredient3)
        ).let { PizzaIngredientEntity.fromMapping(it) }
        pizzaIngredientJoinDao.insertAll(relations)

        val pizza1Ingredients = pizzaIngredientJoinDao.getIngredientsForPizza(pizza1.uid)
        pizza1Ingredients shouldContainAll listOf(ingredient1, ingredient2)

        val pizza2Ingredients = pizzaIngredientJoinDao.getIngredientsForPizza(pizza2.uid)
        pizza2Ingredients shouldContainAll listOf(ingredient1, ingredient2, ingredient3)
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