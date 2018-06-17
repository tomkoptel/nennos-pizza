package com.sample.nennos.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.nennos.R
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.kodein.KodeinFragment
import com.sample.nennos.ktx.arch.observeNonNull
import com.sample.nennos.ktx.formattedPrice
import com.sample.nennos.ktx.load
import com.sample.nennos.ktx.provideModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pizza_detail_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import timber.log.Timber

class PizzaDetailFragment : KodeinFragment() {
    override fun activityModule() =
            Kodein.Module {
                bind<PizzaIngredientsAdapter>() with provider { PizzaIngredientsAdapter(instance()) }
                bind<PizzaDetailViewModel>() with provider {
                    instance<FragmentActivity>().provideModel { PizzaDetailViewModel(instance(), instance()) }
                }
            }

    private val model by instance<PizzaDetailViewModel>()
    private val picasso by instance<Picasso>()
    private val ingredientsAdapter by instance<PizzaIngredientsAdapter>()
    private var toolbar: Toolbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.pizza_detail_fragment, container, false)
        toolbar = activity?.findViewById(R.id.toolbar)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = checkNotNull(arguments) { "Developer error! You should start activity with onPizzaChange id." }
        val details = args.getParcelable("details") as PizzaDetails

        pizzaImage.load(picasso, details.imageUrl)
        updateBuyButton(details.formattedPrice())
        details.name.let {
            toolbar?.title = it
            pizzaImage.contentDescription = it
        }

        recyclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setItemViewCacheSize(20)
            hasFixedSize()
            adapter = ingredientsAdapter
        }

        if (savedInstanceState == null) {
            progressBar.show()
            model.loadPizza(details.id)
        }

        model.onPizzaChange.observeNonNull(viewLifecycleOwner) {
            when (it) {
                is LookupOperation.Success -> {
                    val pizzaChoice = it.data
                    progressBar.hide()

                    ingredientsAdapter.submitList(pizzaChoice.ingredients)
                    updateBuyButton(pizzaChoice.formattedPrice())
                }
                is LookupOperation.Error -> {
                    Timber.e(it.error)
                }
            }
        }
        ingredientsAdapter.onIngredientChoice.observeNonNull(viewLifecycleOwner, model::recalculatePrice)
    }

    private fun updateBuyButton(price: String) {
        buyText.text = HtmlCompat.fromHtml(
                getString(R.string.add_to_cart, price),
                FROM_HTML_MODE_COMPACT
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("any", "dummy")
        super.onSaveInstanceState(outState)
    }
}