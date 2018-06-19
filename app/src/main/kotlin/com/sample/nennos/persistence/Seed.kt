package com.sample.nennos.persistence

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sample.nennos.domain.Pizza

object Seed : RoomDatabase.Callback() {
    val custom = Pizza(
            id = "2bb06763-4519-4404-b074-831901d8f84a",
            name = "Custom Pizza",
            visible = false,
            basePrice = 10.0,
            ingredients = emptyList()
    )

    override fun onCreate(db: SupportSQLiteDatabase) {
        db.apply {
            execSQL("INSERT into Pizza (uid, name, imageUrl, basePrice, visible) VALUES (\"${custom.id}\", \"${custom.name}\", NULL, ${custom.basePrice}, 0);")
        }
    }
}