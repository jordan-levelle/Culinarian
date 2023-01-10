package com.cs683.culinarian.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.databinding.IngredientItemBinding
import com.cs683.culinarian.model.Ingredient
import com.cs683.culinarian.utilities.IngredientItemCallback

class IngredientItemAdapter(
    private val cartListener: (position: Int, cartClicked: Boolean) -> Unit
) : ListAdapter<Ingredient, IngredientItemAdapter.IngredientItemViewHolder>(IngredientItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            IngredientItemViewHolder = IngredientItemViewHolder.inflateFrom(parent, cartListener)

    override fun onBindViewHolder(holder: IngredientItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun refreshList() {
        notifyDataSetChanged()
    }

    class IngredientItemViewHolder(
        val binding: IngredientItemBinding,
        val cartListener: (position: Int, cartClicked: Boolean) -> Unit
        ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun inflateFrom(
                parent: ViewGroup,
                cartListener: (position: Int, cartClicked: Boolean) -> Unit
            ): IngredientItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientItemBinding.inflate(layoutInflater, parent, false)
                return IngredientItemViewHolder(binding, cartListener)
            }
        }

        init {
            binding.cartButton.setOnCheckedChangeListener { cart, cartClicked ->
                cartListener(bindingAdapterPosition, cartClicked)
            }
        }

        fun bind(item: Ingredient){
            binding.ingredient = item
            var amount = item.ingredientAmount.toString()
            if (amount == "0.0") amount = ""
            binding.amount.text = amount.substringBefore(".0")
        }
    }
}