package com.sample.nennos.domain

import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should contain`
import org.junit.Assert.fail
import org.junit.Test
import java.util.concurrent.TimeUnit

class DiskNetworkStoreTest {
    private val timeout = 200L
    private val timeUnit = TimeUnit.MILLISECONDS
    private val testScheduler = TestScheduler()

    private val pizzaFromNet = mockk<Pizza>(name = "pizzaFromNet")
    private val pizzaDiskNet = mockk<Pizza>(name = "pizzaDiskNet")
    private val diskStore = mockk<Store<Pizza>> {
        every { insertAll(any()) } returns Completable.complete()
    }
    private val netStore = mockk<Store<Pizza>>()
    private val repoUnderTest = DiskNetworkStore(diskStore, netStore)

    private lateinit var test: TestObserver<LookupOperation<List<Pizza>>>

    @Test
    fun disk_is_empty_network_returns_items() {
        givenDiskStoreReturns(emptyList())
        givenNetworkStoreReturns(listOf(pizzaFromNet))

        whenGetAll()
        whenNetworkYieldsResult()

        thenShouldSaveResultToDisk(pizzaFromNet)
        thenResponseIsSuccess(pizzaFromNet)
    }

    @Test
    fun disk_is_not_empty_network_is_not_called() {
        givenDiskStoreReturns(listOf(pizzaDiskNet))
        givenNetworkStoreReturns(listOf(pizzaFromNet))

        whenGetAll()
        whenNetworkYieldsResult()

        thenShouldNotSaveOnTheDisk()
        thenResponseIsSuccess(pizzaDiskNet)
    }

    @Test
    fun disk_is_error_network_returns_result() {
        givenDiskStoreReturnsError(IllegalStateException("Any!"))
        givenNetworkStoreReturns(listOf(pizzaFromNet))

        whenGetAll()
        whenNetworkYieldsResult()

        thenShouldSaveResultToDisk(pizzaFromNet)
        thenResponseIsSuccess(pizzaFromNet)
    }

    @Test
    fun disk_is_empty_network_returns_error() {
        val error = IllegalStateException("Any!")
        givenDiskStoreReturns(emptyList())
        givenNetworkStoreReturnsError(error)

        whenGetAll()
        whenNetworkYieldsResult()

        thenShouldNotSaveOnTheDisk()
        thenResponseIsError(error)
    }

    private fun givenDiskStoreReturns(pizzaList: List<Pizza>) {
        every { diskStore.getAll() } returns Single.just(LookupOperation.Success(pizzaList))
    }

    private fun givenDiskStoreReturnsError(error: Throwable) {
        every { diskStore.getAll() } returns Single.just(LookupOperation.Error(error))
    }

    private fun givenNetworkStoreReturns(pizzaList: List<Pizza>) {
        every { netStore.getAll() } returns Single.timer(timeout, timeUnit, testScheduler).map {
            LookupOperation.Success(pizzaList)
        }
    }

    private fun givenNetworkStoreReturnsError(error: Throwable) {
        every { netStore.getAll() } returns Single.timer(timeout, timeUnit, testScheduler).map {
            LookupOperation.Error<List<Pizza>>(error)
        }
    }

    private fun whenNetworkYieldsResult() {
        testScheduler.advanceTimeBy(timeout, timeUnit)
    }

    private fun whenGetAll() {
        test = repoUnderTest.getAll().test()
    }

    private fun thenResponseIsSuccess(pizza: Pizza) {
        test.awaitCount(1)
        test.assertNoErrors()
        test.values().first().let {
            when (it) {
                is LookupOperation.Success -> it.data `should contain` pizza
                is LookupOperation.Error -> fail("Expected success but got error")
            }
        }
    }

    private fun thenResponseIsError(error: Throwable) {
        test.awaitCount(1)
        test.assertNoErrors()
        test.values().first().let {
            when (it) {
                is LookupOperation.Success -> fail("Expected error but got success")
                is LookupOperation.Error -> it.error `should be` error
            }
        }
    }

    private fun thenShouldSaveResultToDisk(pizza: Pizza) {
        verify { diskStore.insertAll(assert { it.contains(pizza) }) }
    }

    private fun thenShouldNotSaveOnTheDisk() {
        verify { diskStore.insertAll(any()) wasNot Called }
    }
}