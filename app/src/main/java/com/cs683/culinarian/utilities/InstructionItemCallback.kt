package com.cs683.culinarian.utilities

import androidx.recyclerview.widget.DiffUtil
import com.cs683.culinarian.model.Instruction

class InstructionItemCallback : DiffUtil.ItemCallback<Instruction>() {
    override fun areItemsTheSame(oldItem: Instruction, newItem: Instruction) = oldItem.instructionId == newItem.instructionId
    override fun areContentsTheSame(oldItem: Instruction, newItem: Instruction) = oldItem == newItem
}