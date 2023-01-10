package com.cs683.culinarian.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.databinding.SearchResultCardBinding
import com.cs683.culinarian.model.Recipe


class AddExistingRecipeAdapter(private val addRecipeCheckedListener: (position : Int) -> Unit)
    : RecyclerView.Adapter<AddExistingRecipeAdapter.ViewHolder>() {

    private val recipes = mutableListOf<Recipe>()

    fun replaceItems(recipeList: List<Recipe>) {
        recipes.clear()
        recipes.addAll(recipeList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchResultCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipes = recipes[position]
        holder.contentView.text = recipes.recipeName
        holder.checkBox.isChecked = false
        holder.checkBox.setOnClickListener {
            addRecipeCheckedListener(position)
        }
    }

    override fun getItemCount(): Int = recipes.size

    fun getRecipe(position: Int): Recipe {
        if (recipes.size > 0)
            return recipes[position]
        else
            return Recipe(0,"")
    }

    inner class ViewHolder(binding: SearchResultCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.recipeName
        val checkBox : CheckBox = binding.checkRecipe

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}