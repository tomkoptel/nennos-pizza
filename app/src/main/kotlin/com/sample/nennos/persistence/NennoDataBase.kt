package com.sample.nennos.persistence

import android.app.Application
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.reactivex.Single

@Database(entities = arrayOf(
        PizzaEntity::class,
        IngredientEntity::class,
        DrinkEntity::class,
        CartEntity::class,
        PizzaIngredientEntity::class,
        CartIngredientEntity::class,
        CartPizzaEntity::class,
        CartDrinkEntity::class
), version = 1)
@TypeConverters(value = arrayOf(TimeConverter::class))
abstract class NennoDataBase : RoomDatabase() {

    abstract fun pizzaDao(): PizzaDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun pizzaIngredientJoinDao(): PizzaIngredientJoinDao
    abstract fun drinkDao(): DrinkDao
    abstract fun cartDao(): CartDao

    class Factory(private val app: Application) {
        @get:WorkerThread
        val database by lazy {
            Room.databaseBuilder(app, NennoDataBase::class.java, "nenno.db").build()
        }

        @UiThread
        fun getDatabase(): Single<NennoDataBase> = Single.fromCallable { database }
    }
}
