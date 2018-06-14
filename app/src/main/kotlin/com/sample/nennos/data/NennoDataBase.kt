package com.sample.nennos.data

import android.content.Context
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.reactivex.Single

@Database(entities = arrayOf(PizzaEntry::class, IngredientEntry::class, DrinkEntry::class), version = 1)
abstract class NennoDataBase : RoomDatabase() {

    abstract fun pizzaDao(): PizzaDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun drinkDao(): DrinkDao

    class Factory(private val appContext: Context) {
        @get:WorkerThread
        val database by lazy {
            Room.databaseBuilder(appContext, NennoDataBase::class.java, "nenno.db").build()
        }

        @UiThread
        fun getDatabase(): Single<NennoDataBase> = Single.fromCallable { database }
    }
}
