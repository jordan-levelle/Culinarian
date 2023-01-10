package com.cs683.culinarian.utilities

import androidx.recyclerview.widget.DiffUtil
import com.cs683.culinarian.model.Ingredient

class IngredientItemCallback : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient) = oldItem.ingredientId == newItem.ingredientId
    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient) = oldItem == newItem
}