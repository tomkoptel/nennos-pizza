package com.sample.nennos.persistence

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * NO-OP solution for production
 */
object Seed : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
    }
}