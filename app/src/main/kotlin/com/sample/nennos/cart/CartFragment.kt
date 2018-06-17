package com.sample.nennos.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.nennos.R
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
        cartViewModel.onRemovedFromCart.observeNonNull(viewLifecycleOwner) { item ->
            recyclerView.context.toast("Removed ${item.name}")
        }
        cartAdapter.onRemoveItem.observeNonNull(viewLifecycleOwner) {
            cartViewModel.removeFromCart(it)
        }
    }
}