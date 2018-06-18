package com.sample.nennos.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.nennos.R
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.kodein.KodeinFragment
import com.sample.nennos.ktx.arch.observeNonNull
import kotlinx.android.synthetic.main.pizza_detail_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import timber.log.Timber

class DrinksListFragment : KodeinFragment() {
    override fun activityModule() = Kodein.Module {
        bind<DrinksAdapter>() with provider { DrinksAdapter(instance()) }
        bind<DrinkViewModel>() with provider { DrinkViewModel() }
    }

    private val drinksAdapter by instance<DrinksAdapter>()
    private val drinkViewModel by instance<DrinkViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drinks_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setItemViewCacheSize(20)
            hasFixedSize()
            adapter = drinksAdapter
        }

        progressBar.show()
        drinkViewModel.onDrinks.observeNonNull(this) {
            progressBar.hide()

            when (it) {
                is LookupOperation.Success -> drinksAdapter.submitList(it.data)
                is LookupOperation.Error -> Timber.e(it.error)
            }
        }
        drinkViewModel.onDrinkAdded.observeNonNull(this) {

        }
        drinksAdapter.onPrimaryAction.observeNonNull(this) {
            drinkViewModel.addDrink(it)
        }
    }
}