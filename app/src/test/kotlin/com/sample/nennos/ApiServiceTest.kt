package com.sample.nennos

import com.sample.nennos.net.ApiService
import io.reactivex.Single
import okreplay.*
import org.amshove.kluent.`should be`
import org.junit.Rule
import org.junit.Test
import retrofit2.adapter.rxjava2.Result
import java.io.File

class ApiServiceTest {
    private val okReplayInterceptor = OkReplayInterceptor()
    private val resourcesFolder = javaClass.classLoader.getResource("dummy.txt").run { File(this!!.path).parentFile }
    private val okReplayConfig = OkReplayConfig.Builder()
            .tapeRoot(DefaultTapeRoot(resourcesFolder))
            .interceptor(okReplayInterceptor)
            .sslEnabled(true)
            .defaultMode(TapeMode.READ_ONLY)
            .build()
    @get:Rule
    val recorderRule = RecorderRule(okReplayConfig)

    private val apiUnderTest = ApiService.create {
        it.newBuilder().addInterceptor(okReplayInterceptor).build()
    }

    @Test
    @OkReplay
    fun test_getIngredients() {
        apiUnderTest.getIngredients().assertSuccess()
    }

    @Test
    @OkReplay
    fun test_getDrinks() {
        apiUnderTest.getDrinks().assertSuccess()
    }

    @Test
    @OkReplay
    fun test_getPizza() {
        apiUnderTest.getPizza().assertSuccess()
    }

    private fun <R> Single<Result<R>>.assertSuccess() {
        test().apply {
            assertNoErrors()
            awaitCount(1)

            values().first().error() `should be` null
        }
    }
}