package com.sample.nennos.cart

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.nennos.R
import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.Pizza
import com.sample.nennos.kodein.KodeinFragment
import com.sample.nennos.ktx.arch.observeNonNull
import com.sample.nennos.ktx.formattedPrice
import com.sample.nennos.widget.CartViewModel
import kotlinx.android.synthetic.main.cart_detail_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class CartFragment : KodeinFragment() {
    override fun activityModule() =
            Kodein.Module {
                bind<CartItemsAdapter>() with provider { CartItemsAdapter(instance()) }
                bind<CartViewModel>() with provider { CartViewModel(instance(), instance()) }
            }

    private val cartAdapter by instance<CartItemsAdapter>()
    private val cartViewModel by instance<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cart_menu, menu)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cart_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setItemViewCacheSize(20)
            hasFixedSize()
            adapter = cartAdapter
        }

        cartViewModel.cartObservable.observeNonNull(this) {
            val items = it.items
            val noItems = items.isEmpty()

            recyclerView.visibility = if (noItems) View.GONE else View.VISIBLE
            cartIsEmpty.visibility = if (noItems) View.VISIBLE else View.GONE

            payButton.setPrice(it.formattedPrice())
            cartAdapter.submitList(items)
        }
        cartAdapter.onRemoveItem.observeNonNull(viewLifecycleOwner) {
            when (it) {
                is Pizza -> cartViewModel.removeFromCart(it)
                is Drink -> cartViewModel.removeFromCart(it)
            }
        }
    }
}