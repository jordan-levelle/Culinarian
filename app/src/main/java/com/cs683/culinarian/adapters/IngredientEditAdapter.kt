package com.cs683.culinarian.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.databinding.IngredientEditBinding
import com.cs683.culinarian.utilities.IngredientItemCallback
import com.cs683.culinarian.model.Ingredient

class IngredientEditAdapter(
    private val changeListener: (position: Int, amount: String) -> Unit,
    private val moveListener: (from: Int, to: Int) -> Unit,
    private val deleteListener: (position: Int) -> Unit
) : ListAdapter<Ingredient, IngredientEditAdapter.IngredientEditViewHolder>(IngredientItemCallback()){

    //create view holder for each ingredient
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            IngredientEditViewHolder = IngredientEditViewHolder.inflateFrom(parent, changeListener, deleteListener)

    //bind data to each view holder
    override fun onBindViewHolder(holder: IngredientEditViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    //notify viewmodel when item dragged on screen
    fun onItemMoved(from: Int, to: Int) {
        moveListener(from, to)
        notifyItemMoved(from, to)
    }

    //notify recycler view that ingredient was inserted
    fun onItemAdded() {
        notifyItemInserted(currentList.size - 1)
    }

    //notify viewModel item is deleted, then update recycler view
    fun onItemDeleted(position: Int) {
        notifyItemRemoved(position)
    }

    class IngredientEditViewHolder(
        val binding: IngredientEditBinding,
        val changeListener: (position: Int, amount: String) -> Unit,
        val deleteListener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), TextWatcher {

        //use companion object to initialize each view holder
        companion object {
            fun inflateFrom(
                parent: ViewGroup,
                changeListener: (position: Int, amount: String) -> Unit,
                deleteListener: (position: Int) -> Unit
            ): IngredientEditViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientEditBinding.inflate(layoutInflater, parent, false)
                return IngredientEditViewHolder(binding, changeListener, deleteListener)
            }
        }

        init {
            binding.amount.addTextChangedListener(this)
            binding.deleteButton.setOnClickListener {
                deleteListener(bindingAdapterPosition)
            }
        }

        //assign data to variables in layout
        fun bind(item: Ingredient) {
            binding.ingredient = item
            var amount = item.ingredientAmount.toString()
            if (amount == "0.0") amount = ""
            amount = amount.substringBefore(".0")
            binding.amount.setText(amount)
        }

        //TextWatcher implementation
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            var amount = binding.amount.text.toString()
            if (amount == "") amount = "0"
            changeListener(bindingAdapterPosition, amount)
        }
    }
}