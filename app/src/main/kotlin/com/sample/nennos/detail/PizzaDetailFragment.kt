package com.sample.nennos.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sample.nennos.R

class PizzaDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pizza_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = checkNotNull(arguments) { "Developer error! You should start activity with pizza id." }
        val safeArgs = PizzaDetailFragmentArgs.fromBundle(args)

        Snackbar.make(view!!, safeArgs.pizzaId, Snackbar.LENGTH_SHORT).show()
    }
}