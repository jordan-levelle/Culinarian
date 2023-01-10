package com.cs683.culinarian.utilities

import androidx.appcompat.widget.SearchView

// inline is for efficiency
// crossinline is required

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}