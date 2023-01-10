package com.cs683.culinarian.utilities

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.adapters.InstructionEditAdapter

class InstructionDragCallback: ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    0
) {
    //notify adapter that item is moved
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val adapter = recyclerView.adapter as InstructionEditAdapter
        val from = viewHolder.bindingAdapterPosition
        val to = target.bindingAdapterPosition
        adapter.onItemMoved(from, to)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
}