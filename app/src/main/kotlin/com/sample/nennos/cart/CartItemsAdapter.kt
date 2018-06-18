package com.sample.nennos.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.nennos.R
import com.sample.nennos.domain.Item
import com.sample.nennos.ktx.arch.toLiveData
import com.sample.nennos.ktx.formattedPrice
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cart_item_row.*

class CartItemsAdapter(private val layoutInflater: LayoutInflater) : ListAdapter<Item, ItemViewHolder>(ItemDiffCallback) {
    private val publisher: FlowableProcessor<Item> = PublishProcessor.create<Item>().toSerialized()

    val onRemoveItem: LiveData<Item> = publisher.toLiveData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return layoutInflater.inflate(R.layout.cart_item_row, parent, false).let {
            ItemViewHolder(it, publisher)
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ItemViewHolder(override val containerView: View, private val publisher: FlowableProcessor<Item>) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(item: Item) {
        itemName.text = item.name
        itemPrice.text = item.formattedPrice()
        primaryAction.setOnClickListener {
            publisher.onNext(item)
        }
    }
}

private object ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}