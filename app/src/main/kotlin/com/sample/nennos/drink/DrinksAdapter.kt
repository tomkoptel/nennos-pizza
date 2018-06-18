package com.sample.nennos.drink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.nennos.R
import com.sample.nennos.domain.Drink
import com.sample.nennos.ktx.arch.ActionLiveData
import com.sample.nennos.ktx.formattedPrice
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cart_item_row.*

class DrinksAdapter(private val layoutInflater: LayoutInflater) : ListAdapter<Drink, DrinkViewHolder>(DrinkDiffCallback) {
    private val publisher: MutableLiveData<Drink> = ActionLiveData()

    val onPrimaryAction: LiveData<Drink> = publisher

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        return layoutInflater.inflate(R.layout.cart_item_row, parent, false).let {
            DrinkViewHolder(it, publisher)
        }
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DrinkViewHolder(override val containerView: View, private val publisher: MutableLiveData<Drink>) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(drink: Drink) {
        itemName.text = drink.name
        itemPrice.text = drink.formattedPrice()
        primaryAction.setOnClickListener {
            publisher.value = drink
        }
    }
}

private object DrinkDiffCallback : DiffUtil.ItemCallback<Drink>() {
    override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldDrink: Drink, newDrink: Drink): Boolean {
        return oldDrink == newDrink
    }
}