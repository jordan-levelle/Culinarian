package com.cs683.culinarian.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.databinding.FragmentRecipesListItemBinding
import com.cs683.culinarian.model.Recipe


class RecipesListViewAdapter(
    private val onRecipeClickListener:(recipeId: Long) ->  Unit)
    : RecyclerView.Adapter<RecipesListViewAdapter.ViewHolder>() {

    private var recipes = mutableListOf<Recipe>()

    fun replaceRecipes(recipesList: List<Recipe>) {
        recipes.clear()
        recipes.addAll(recipesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentRecipesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.contentView.text = recipe.recipeName
        holder.cardView.setOnClickListener{
            onRecipeClickListener(recipe.recipeId)
        }
    }

    override fun getItemCount(): Int = recipes.size

    fun getRecipe(pos: Int): Recipe {
        if (recipes.size > 0)
            return recipes[pos]
        else
            return Recipe(0, "")
    }

    inner class ViewHolder(binding: FragmentRecipesListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.recipeName
        val cardView: CardView = binding.recipesCard

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}