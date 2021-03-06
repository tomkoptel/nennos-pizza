package com.sample.nennos.net

import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    companion object Factory {
        fun create(decorateOkHttp: (OkHttpClient) -> OkHttpClient = { OkHttpClient() }): ApiService {
            val defaultMoshi = Moshi.Builder().build()
            val moshi = Moshi.Builder()
                    .add(DrinkData::class.java, DrinkDataJsonAdapter(defaultMoshi))
                    .add(IngredientData::class.java, IngredientDataJsonAdapter(defaultMoshi))
                    .add(PizzaData::class.java, PizzaDataJsonAdapter(defaultMoshi))
                    .add(PizzasData::class.java, PizzasDataJsonAdapter(defaultMoshi))
                    .build()
            val okHttpClient = decorateOkHttp(OkHttpClient())
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.myjson.com/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .client(okHttpClient)
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }

    @GET("/bins/ozt3z")
    fun getIngredients(): Single<Result<List<IngredientData>>>

    @GET("/bins/150da7")
    fun getDrinks(): Single<Result<List<DrinkData>>>

    @GET("/bins/dokm7")
    fun getPizzas(): Single<Result<PizzasData>>

    @POST
    fun postCheckOut(@Url destination: String, @Body cart: CartData): Completable
}

fun ApiService.checkOut(cart: CartData) = postCheckOut("https://httpbin.org/post", cart)