package com.sample.nennos.net

import com.sample.nennos.domain.LookupOperation
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.junit.Test
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

class NetPizzaStoreRepoTest {
    private val mockApiService = mockk<ApiService>()
    private val repoUnderTest = NetPizzaStore(apiService = mockApiService)

    private val error = HttpServerError("Fail!")
    private val pizza = PizzaData(name = "Chili Pizza", ingredients = listOf(1, 2))
    private val pizzaData = PizzasData(pizzas = listOf(pizza), basePrice = 4.0)
    private val ingredient1 = IngredientData(name = "Chili pepper", price = 1.0, id = 1)
    private val ingredient2 = IngredientData(name = "Salami", price = 2.0, id = 2)
    private val ingredient3 = IngredientData(name = "Tofu", price = 5.0, id = 3)
    private val ingredients = listOf(ingredient1, ingredient2, ingredient3)

    @Test
    fun test_ingredients_lookup_fails() {
        givenGetIngredientsCallFails()
        givenGetPizzaCallSucceeds()

        repoUnderTest.getAll().test().apply {
            assertNoErrors()

            val response = values().first()
            response `should be instance of` LookupOperation.Error::class
        }
    }

    @Test
    fun test_pizza_lookup_fails() {
        givenGetIngredientsCallSucceeds()
        givenGetPizzaCallFails()

        repoUnderTest.getAll().test().apply {
            assertNoErrors()

            val response = values().first()
            response `should be instance of` LookupOperation.Error::class
        }
    }

    @Test
    fun test_both_lookup_fail() {
        givenGetIngredientsCallFails()
        givenGetPizzaCallFails()

        repoUnderTest.getAll().test().apply {
            assertNoErrors()

            val response = values().first()
            response `should be instance of` LookupOperation.Error::class
        }
    }

    @Test
    fun test_both_lookup_succeeds() {
        givenGetIngredientsCallSucceeds()
        givenGetPizzaCallSucceeds()

        repoUnderTest.getAll().test().apply {
            assertNoErrors()

            val response = values().first() as LookupOperation.Success
            val pizza = response.data.first()

            pizza.basePrice `should equal` 4.0
            pizza.name `should be` "Chili Pizza"

            val ingredients = pizza.ingredients
            ingredients.size `should be` 2

            val first = ingredients.first()
            first.name `should be` "Chili pepper"
            first.price `should equal` 1.0

            val last = ingredients.last()
            last.name `should be` "Salami"
            last.price `should equal` 2.0
        }
    }

    private fun givenGetPizzaCallFails() {
        every { mockApiService.getPizzas() } returns Single.just(Result.error(error))
    }

    private fun givenGetPizzaCallSucceeds() {
        every { mockApiService.getPizzas() } returns Single.just(Result.response(Response.success(pizzaData)))
    }

    private fun givenGetIngredientsCallFails() {
        every { mockApiService.getIngredients() } returns Single.just(Result.error(error))
    }

    private fun givenGetIngredientsCallSucceeds() {
        every { mockApiService.getIngredients() } returns Single.just(Result.response(Response.success(ingredients)))
    }
}