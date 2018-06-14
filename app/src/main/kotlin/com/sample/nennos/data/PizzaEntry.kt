package com.sample.nennos.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Pizza")
data class PizzaEntry(
        @PrimaryKey var uid: String,
        var name: String
) {
    constructor() : this(
            uid = UUID.randomUUID().toString(),
            name = ""
    )

    constructor(name: String) : this(
            uid = UUID.randomUUID().toString(),
            name = name
    )
}