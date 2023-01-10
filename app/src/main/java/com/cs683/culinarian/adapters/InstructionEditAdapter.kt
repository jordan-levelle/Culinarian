package com.cs683.culinarian.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.databinding.InstructionEditBinding
import com.cs683.culinarian.model.Instruction
import com.cs683.culinarian.utilities.InstructionItemCallback

class InstructionEditAdapter(
    val changeListener: (position: Int, instruction:String) -> Unit,
    val moveListener: (from: Int, to: Int) -> Unit,
    val deleteListener: (position: Int) -> Unit
) : ListAdapter<Instruction, InstructionEditAdapter.InstructionEditViewHolder>(InstructionItemCallback()) {

    //create view holder for each ingredient
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            InstructionEditViewHolder = InstructionEditViewHolder.inflateFrom(parent, changeListener, deleteListener)

    //bind data to each view holder
    override fun onBindViewHolder(holder: InstructionEditViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    //notify viewmodel when item dragged on screen
    fun onItemMoved(from: Int, to: Int) {
        moveListener(from, to)
        notifyItemMoved(from, to)
    }

    fun onItemAdded() {
        notifyItemInserted(currentList.size - 1)
    }

    fun onItemDeleted(position: Int) {
        notifyItemRemoved(position)
    }

    class InstructionEditViewHolder(
        val binding: InstructionEditBinding,
        val changeListener: (position: Int, instruction: String) -> Unit,
        val deleteListener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), TextWatcher {

        //use companion object to initialize each view holder
        companion object {
            fun inflateFrom(
                parent: ViewGroup,
                changeListener: (position: Int, ingredient: String) -> Unit,
                deleteListener: (position: Int) -> Unit
            ): InstructionEditViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = InstructionEditBinding.inflate(layoutInflater, parent, false)
                return InstructionEditViewHolder(binding, changeListener, deleteListener)
            }
        }

        init {
            binding.instructionEdit.addTextChangedListener(this)
            binding.deleteButton.setOnClickListener {
                deleteListener(bindingAdapterPosition)
            }
        }

        //assign data to variables in layout
        fun bind(item: Instruction, position: Int) {
            val displayPosition = item.position + 1
            binding.ingredientPosition.text = displayPosition.toString()
            binding.instruction = item.instructionText
        }

        //TextWatcher implementation
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            changeListener(bindingAdapterPosition, p0.toString())
        }
    }
}