package com.cs683.culinarian.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.databinding.FragmentRecipesListItemBinding
import com.cs683.culinarian.model.Recipe


class SearchAdapter(val onRecipeClickListener : (recipeId: Long) -> Unit)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val recipe = mutableListOf<Recipe>()

    fun replaceItems(myRecipes: List<Recipe>) {
        recipe.clear()
        recipe.addAll(myRecipes)
        notifyDataSetChanged()
    }

//    fun reAddItem(recipe: Recipe) {
//        uncheckedRecipe.add(recipe)
//        notifyItemInserted(uncheckedRecipe.size -1)
//    }

//    fun itemCheck (recipeCheck: Recipe) {
//        val index = recipe.indexOf(recipeCheck)
//        recipe.removeAt(index)
//        notifyItemRemoved(index)
//        notifyItemRangeChanged(index, recipe.size)
//    }

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
        val recipe = recipe[position]
        holder.contentView.text = recipe.recipeName
//        holder.checkBox.isChecked = false
//        holder.checkBox.setOnClickListener{
//            checkListener(position)
//            notifyItemRemoved(position)
//        }
        holder.cardView.setOnClickListener {
            onRecipeClickListener(recipe.recipeId)
        }
    }

    override fun getItemCount(): Int = recipe.size

    inner class ViewHolder(binding: FragmentRecipesListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.recipeName
//        val checkBox: CheckBox = binding.checkRecipe
        val cardView: CardView = binding.recipesCard

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}