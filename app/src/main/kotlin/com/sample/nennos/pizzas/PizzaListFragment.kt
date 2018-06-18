package com.sample.nennos.pizzas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.nennos.R
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.domain.Pizza
import com.sample.nennos.domain.toCartItem
import com.sample.nennos.kodein.KodeinFragment
import com.sample.nennos.ktx.arch.observeNonNull
import com.sample.nennos.ktx.provideModel
import com.sample.nennos.widget.CartViewModel
import kotlinx.android.synthetic.main.pizza_list_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import timber.log.Timber

class PizzaListFragment : KodeinFragment() {
    override fun activityModule() =
            Kodein.Module {
                bind<PizzaAdapter>() with provider { PizzaAdapter(instance(), instance()) }
                bind<PizzaListViewModel>() with provider {
                    instance<FragmentActivity>().provideModel { PizzaListViewModel(instance(Pizza::class), instance()) }
                }
                bind<CartViewModel>() with provider {
                    instance<FragmentActivity>().provideModel { CartViewModel(instance(), instance()) }
                }
            }

    private val pizzaListViewModel by instance<PizzaListViewModel>()
    private val pizzaAdapter by instance<PizzaAdapter>()
    private val cartViewModel by instance<CartViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pizza_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressBar.visibility = View.VISIBLE
        progressBar.show()

        recyclerView.apply {
            adapter = pizzaAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setItemViewCacheSize(20)
            hasFixedSize()
        }

        pizzaListViewModel.pizzaList.observeNonNull(this) {
            when (it) {
                is LookupOperation.Success -> {
                    recyclerView.visibility = View.VISIBLE
                    errorText.visibility = View.GONE

                    progressBar.hide()
                    progressBar.visibility = View.GONE

                    pizzaAdapter.submitList(it.data)
                }
                is LookupOperation.Error -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    errorText.visibility = View.VISIBLE

                    it.error.let {
                        Timber.e(it)
                        errorText.text = it.message
                    }
                }
            }
        }
        cartViewModel.onAddToCart.observeNonNull(viewLifecycleOwner) { item ->
            recyclerView.context.toast("Saved ${item.name}")
        }

        pizzaAdapter.addToCartTaps.observeNonNull(viewLifecycleOwner) {
            cartViewModel.addToCart(it.toCartItem())
        }
    }
}