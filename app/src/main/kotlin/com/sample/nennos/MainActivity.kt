package com.sample.nennos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.nennos.domain.LookupOperation
import com.sample.nennos.ktx.arch.observeNonNull
import com.sample.nennos.ktx.provideModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import timber.log.Timber

class MainActivity : KodeinActivity() {
    override fun activityModule() =
            Kodein.Module {
                bind<PizzaAdapter>() with provider { PizzaAdapter(instance(), instance()) }
                bind<MainViewModel>() with provider {
                    instance<FragmentActivity>().provideModel { MainViewModel(instance(), instance()) }
                }
            }

    private val model by instance<MainViewModel>()
    private val pizzaAdapter by instance<PizzaAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        progressBar.visibility = View.VISIBLE
        progressBar.show()

        recyclerView.apply {
            adapter = pizzaAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setItemViewCacheSize(20)
            hasFixedSize()
        }

        model.pizzaList.observeNonNull(this) {
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
    }
}
