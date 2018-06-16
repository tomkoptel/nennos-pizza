package com.sample.nennos.pizzas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.nennos.R
import com.sample.nennos.domain.Pizza
import com.sample.nennos.ktx.formattedPrice
import com.sample.nennos.ktx.load
import com.squareup.picasso.Picasso

class PizzaAdapter(private val inflater: LayoutInflater, private val picasso: Picasso) : ListAdapter<Pizza, PizzaAdapter.PizzaHolder>(PizzaDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaHolder {
        val viewGroup = inflater.inflate(R.layout.pizza_item_row, parent, false)
        return PizzaHolder(viewGroup, picasso)
    }

    override fun onBindViewHolder(holder: PizzaHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PizzaHolder(itemView: View, private val picasso: Picasso) : RecyclerView.ViewHolder(itemView) {
        private val pizzaName by lazy(LazyThreadSafetyMode.NONE) { itemView.findViewById<TextView>(R.id.pizzaName) }
        private val pizzaIngredients by lazy(LazyThreadSafetyMode.NONE) { itemView.findViewById<TextView>(R.id.ingredientsList) }
        private val pizzaImage by lazy(LazyThreadSafetyMode.NONE) { itemView.findViewById<ImageView>(R.id.pizzaImage) }
        private val payButton by lazy(LazyThreadSafetyMode.NONE) { itemView.findViewById<Button>(R.id.payButton) }

        fun bind(pizza: Pizza) {
            pizzaName.text = pizza.name
            payButton.text = pizza.formattedPrice()
            pizzaIngredients.text = pizza.ingredientNames
            pizzaImage.contentDescription = pizza.name
            pizzaImage.load(picasso, pizza.imageUrl)
        }
    }
}

object PizzaDiffCallback : DiffUtil.ItemCallback<Pizza>() {
    override fun areItemsTheSame(oldItem: Pizza, newItem: Pizza): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Pizza, newItem: Pizza): Boolean {
        return oldItem == newItem
    }
}