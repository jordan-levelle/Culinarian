package com.cs683.culinarian.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.databinding.SearchResultCardBinding
import com.cs683.culinarian.model.Recipe


class CheckedRecipeAdapter(val uncheckListener : (pos : Int) -> Unit)
    : RecyclerView.Adapter<CheckedRecipeAdapter.CheckRecipeViewHolder>()
{
    private val checkedRecipes = mutableListOf<Recipe>()


    fun addItem(recipe: Recipe) {
       checkedRecipes.add(recipe)
        notifyItemInserted(checkedRecipes.size - 1)

   }

    fun itemUncheck(recipe: Recipe){
        val index = checkedRecipes.indexOf(recipe)
        checkedRecipes.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, checkedRecipes.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckedRecipeAdapter.CheckRecipeViewHolder {
        return CheckRecipeViewHolder(
            SearchResultCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CheckedRecipeAdapter.CheckRecipeViewHolder, position: Int) {
        val recipe = checkedRecipes[position]
        holder.contentView.text = recipe.recipeName
        holder.checkBox.isChecked = true
        holder.checkBox.setOnClickListener {
            uncheckListener(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = checkedRecipes.size


    inner class CheckRecipeViewHolder(binding: SearchResultCardBinding ) : RecyclerView.ViewHolder(binding.root){
        val contentView: TextView = binding.recipeName
        val checkBox :CheckBox = binding.checkRecipe

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}


