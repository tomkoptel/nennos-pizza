package com.sample.nennos.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.nennos.R
import com.sample.nennos.ktx.arch.toLiveData
import com.sample.nennos.ktx.formattedPrice
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.ingredient_item_row.*
import java.util.concurrent.TimeUnit

class PizzaIngredientsAdapter(
        private val inflater: LayoutInflater
) : ListAdapter<IngredientChoice, IngredientViewHolder>(IngredientDiffCallback) {
    private val publisher = PublishProcessor.create<IngredientChoice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = inflater.inflate(R.layout.ingredient_item_row, parent, false)
        return IngredientViewHolder(view, publisher)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    val onIngredientChoice: LiveData<IngredientChoice> by lazy(LazyThreadSafetyMode.NONE) {
        publisher.hide().debounce(200, TimeUnit.MILLISECONDS).toLiveData()
    }
}

class IngredientViewHolder(
        override val containerView: View,
        private val publisher: FlowableProcessor<IngredientChoice>
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(choice: IngredientChoice) {
        val (ingredient, checked) = choice

        ingredientName.text = ingredient.name
        ingredientPrice.text = ingredient.formattedPrice()

        checkBox.apply {
            isChecked = checked
            setOnCheckedChangeListener { _, checked ->
                val copy = choice.copy(checked = checked)
                publisher.onNext(copy)
            }
        }
    }
}

private object IngredientDiffCallback : DiffUtil.ItemCallback<IngredientChoice>() {
    override fun areItemsTheSame(oldItem: IngredientChoice, newItem: IngredientChoice): Boolean {
        return oldItem.ingredient.id == oldItem.ingredient.id
    }

    override fun areContentsTheSame(oldItem: IngredientChoice, newItem: IngredientChoice): Boolean {
        return oldItem == newItem
    }
}