package com.cs683.culinarian.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.databinding.FragmentFolderItemBinding

import com.cs683.culinarian.model.Folder


class FolderRecyclerViewAdapter (private val onFolderClickListener: (folder: Folder) -> Unit)
    : RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder>()
{
    private val folders = mutableListOf<Folder>()

    fun replaceItems(myFolder: List<Folder>) {
        folders.clear()
        folders.addAll(myFolder)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentFolderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folders[position]
        holder.contentView.text = folder.folderName
        holder.cardView.setOnClickListener{

            onFolderClickListener(folder)
        }
    }

    override fun getItemCount(): Int = folders.size

    fun getFolder(pos: Int): Folder {
        if (folders.size > 0)
            return folders[pos]
        else
            return Folder(0, "")
    }

    inner class ViewHolder(binding: FragmentFolderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.folderTitleCard
        val cardView: CardView = binding.folderCard

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}




