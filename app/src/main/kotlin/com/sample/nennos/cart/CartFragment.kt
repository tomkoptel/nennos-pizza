package com.sample.nennos.cart

import android.os.Bundle
import android.view.*
import androidx.core.widget.toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.nennos.R
import com.sample.nennos.domain.Cart
import com.sample.nennos.domain.Drink
import com.sample.nennos.domain.Pizza
import com.sample.nennos.kodein.KodeinFragment
import com.sample.nennos.ktx.arch.observeNonNull
import com.sample.nennos.ktx.formattedPrice
import com.sample.nennos.widget.*
import kotlinx.android.synthetic.main.cart_detail_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import timber.log.Timber

private const val CHECKOUT_TAG = "CHECKOUT_TAG"

class CartFragment : KodeinFragment() {
    override fun activityModule() =
            Kodein.Module(name = "CartModule") {
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

        progressBar.show()
        cartViewModel.cartObservable.observeNonNull(this) {
            val items = it.items
            val noItems = items.isEmpty()

            recyclerView.visibility = if (noItems) View.GONE else View.VISIBLE
            cartIsEmpty.visibility = if (noItems) View.VISIBLE else View.GONE

            payButton.setPrice(it.formattedPrice())
            payButton.tag = it

            recyclerView.visibility = View.VISIBLE
            cartAdapter.submitList(items)

            progressBar.hide()
        }
        cartAdapter.onRemoveItem.observeNonNull(viewLifecycleOwner) {
            when (it) {
                is Pizza -> cartViewModel.removeFromCart(it)
                is Drink -> cartViewModel.removeFromCart(it)
            }
        }
        payButton.setOnClickListener {
            showBlockingDialog()
            cartViewModel.checkOut(it.tag as Cart)
        }
        cartViewModel.onCheckOutResult.observeNonNull(viewLifecycleOwner) {
            hideBlockingDialog()

            when (it) {
                is CheckOutResult.Success -> {
                    context?.toast(R.string.checkout_success)
                    recyclerView.findNavController().popBackStack()
                }
                is CheckOutResult.Error -> {
                    context?.toast(R.string.checkout_failure)
                    Timber.e(it.data)
                }
            }
        }

        (activity as FragmentActivity).observeDialogEvents {
            if (it is DialogEvent.OnShow) {
                it.dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
        }
    }

    private fun showBlockingDialog() {
        fragmentManager?.let {
            dialog(Arguments(
                    customLayout = R.layout.dialog_progress_view,
                    cancellable = true,
                    cancellableOnOutside = true
            )).show(it, CHECKOUT_TAG)
        }
    }

    private fun hideBlockingDialog() {
        (fragmentManager?.findFragmentByTag(CHECKOUT_TAG) as DialogFragment).dismiss()
    }
}
