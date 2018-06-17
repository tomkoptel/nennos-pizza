package com.sample.nennos.pizzas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.nennos.R
import com.sample.nennos.domain.Pizza
import com.sample.nennos.ktx.arch.ActionLiveData
import com.sample.nennos.ktx.formattedPrice
import com.sample.nennos.ktx.load
import com.sample.nennos.ktx.toParcelable
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.pizza_item_row.*

class PizzaAdapter(private val inflater: LayoutInflater, private val picasso: Picasso) : ListAdapter<Pizza, PizzaHolder>(PizzaDiffCallback) {
    private val publisher: MutableLiveData<Pizza> = ActionLiveData()

    val addToCartTaps: LiveData<Pizza> = publisher

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaHolder {
        val viewGroup = inflater.inflate(R.layout.pizza_item_row, parent, false)
        return PizzaHolder(viewGroup, picasso, publisher)
    }

    override fun onBindViewHolder(holder: PizzaHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PizzaHolder(
        override val containerView: View,
        private val picasso: Picasso,
        private val publisher: MutableLiveData<Pizza>
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(pizza: Pizza) {
        pizzaName.text = pizza.name
        payButton.text = pizza.formattedPrice()
        ingredientsList.text = pizza.ingredientNames

        pizzaImage.contentDescription = pizza.name
        pizzaImage.load(picasso, pizza.imageUrl)

        itemView.setOnClickListener {
            val args = bundleOf("details" to pizza.toParcelable())
            findNavController(it).navigate(R.id.show_pizza_detail, args)
        }
        payButton.setOnClickListener {
            publisher.value = pizza
        }
    }
}

private object PizzaDiffCallback : DiffUtil.ItemCallback<Pizza>() {
    override fun areItemsTheSame(oldItem: Pizza, newItem: Pizza): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Pizza, newItem: Pizza): Boolean {
        return oldItem == newItem
    }
}