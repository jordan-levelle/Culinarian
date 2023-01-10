package com.cs683.culinarian.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.R
import com.cs683.culinarian.model.Instruction
import com.cs683.culinarian.utilities.InstructionItemCallback

class InstructionItemAdapter :
    ListAdapter<Instruction, InstructionItemAdapter.InstructionItemViewHolder>(InstructionItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            InstructionItemViewHolder = InstructionItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: InstructionItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class InstructionItemViewHolder(val rootView: TextView) : RecyclerView.ViewHolder(rootView) {
        companion object {
            fun inflateFrom(parent: ViewGroup): InstructionItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.instruction_item, parent, false) as TextView
                return InstructionItemViewHolder(view)
            }
        }

        fun bind(item: Instruction){
            val instruction = "${bindingAdapterPosition + 1}: ${item.instructionText}"
            rootView.text = instruction
        }
    }
}