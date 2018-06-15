package com.sample.nennos

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.nennos.domain.Pizza

class PizzaAdapter(private val inflater: LayoutInflater) : ListAdapter<Pizza, PizzaAdapter.PizzaHolder>(PizzaDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaHolder {
        val textView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        return PizzaHolder(textView as TextView)
    }

    override fun onBindViewHolder(holder: PizzaHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PizzaHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
        fun bind(pizza: Pizza) {
            textView.text = pizza.toString()
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