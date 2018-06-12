package com.sample.nennos

import io.mockk.mockk
import org.amshove.kluent.`should be equal to`
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    private val any = mockk<Anything>()

    @Test
    fun addition_isCorrect() {
        2 + 2 `should be equal to` 4
    }

    class Anything{
        fun doSmth() {
        }
    }
}
