package com.sample.nennos.persistence

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.sample.nennos.domain.Cart
import com.sample.nennos.domain.Item
import com.sample.nennos.domain.LookupOperation
import io.reactivex.Single
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqual
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.threeten.bp.OffsetDateTime

class RoomCartRepoTest {
    private lateinit var database: NennoDataBase
    private lateinit var roomStore: RoomCartRepo

    private val cartDao by lazy { database.cartDao() }

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                NennoDataBase::class.java)
                .allowMainThreadQueries()
                .build()
        roomStore = RoomCartRepo { Single.just(database) }
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_saveCart() {
        val item1 = Item(name = "Pizza 1", price = 10.0)
        val item2 = Item(name = "Pizza 2", price = 12.0)
        val cart = Cart(items = listOf(item1, item2), checkedOutAt = OffsetDateTime.now().plusHours(1))

        roomStore.saveCart(cart).test().apply {
            awaitTerminalEvent()
            assertNoErrors()
        }

        val cartsItemsFromDb = cartDao.getCarts()
        val cartItemsFromDb = cartsItemsFromDb.first()
        val cartFromDb = cartItemsFromDb.cart!!

        cartFromDb.uid shouldBeEqualTo cart.id
        cartFromDb.createdAt shouldEqual cart.createdAt
        cartFromDb.checkedOutAt shouldEqual cart.checkedOutAt

        val itemsFromDb = cartItemsFromDb.items

        val item1FromDb = itemsFromDb.find { it.uid == item1.id }!!
        val item2FromDb = itemsFromDb.find { it.uid == item2.id }!!

        item1FromDb.name shouldBeEqualTo item1.name
        item1FromDb.price shouldEqual item1.price

        item2FromDb.name shouldBeEqualTo item2.name
        item2FromDb.price shouldEqual item2.price
    }

    @Test
    fun test_getRecentCart() {
        val completedCart = Cart(
                createdAt = OffsetDateTime.now().minusHours(1),
                checkedOutAt = OffsetDateTime.now().plusHours(1)
        )
        val currentCart1 = Cart(createdAt = OffsetDateTime.now().minusHours(1))
        val currentCart2 = Cart(createdAt = OffsetDateTime.now())

        cartDao.insertCart(completedCart.toDataObject())
        cartDao.insertCart(currentCart1.toDataObject())
        cartDao.insertCart(currentCart2.toDataObject())

        roomStore.getRecentCart().test().apply {
            assertNoErrors()
            awaitCount(1)

            val lookup = values().first()
            when (lookup) {
                is LookupOperation.Success<Cart> -> lookup.data shouldEqual currentCart2
                is LookupOperation.Error<Cart> -> throw lookup.error
            }
        }
    }
}