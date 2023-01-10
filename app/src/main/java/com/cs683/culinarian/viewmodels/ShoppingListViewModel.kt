package com.cs683.culinarian.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cs683.culinarian.CulinarianApplication
import kotlinx.coroutines.launch

//TODO: clear all button
class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {
    val repository = (application as CulinarianApplication).repository

    val shoppingList = repository.getShoppingList()

    //stores recipeId and ingredientId of removed ingredient
    private val _cartClicked = MutableLiveData<Pair<Long, Long>?>(null)
    val cartClicked: LiveData<Pair<Long, Long>?> get() = _cartClicked

    private val _cartCleared = MutableLiveData<Boolean?>()
    val cartCleared: LiveData<Boolean?> get() = _cartCleared

    fun clearAllClicked() {
        val ingredients = shoppingList.value!!
        for (ingredient in ingredients) ingredient.inShoppingList = false
        viewModelScope.launch {
            repository.editIngredientList(ingredients)
        }
        _cartCleared.value = true
    }

    fun clearHandled() {
        _cartCleared.value = null
    }

    //TODO: check before db edit
    fun shoppingCartClicked(position: Int, cartClicked: Boolean) {
        val ingredient = shoppingList.value!![position]
        viewModelScope.launch {
            ingredient.inShoppingList = cartClicked
            repository.editIngredient(ingredient)
        }
        _cartClicked.value = Pair(ingredient.recipeOwnerId, ingredient.ingredientId)
    }

    fun cartClickHandled() {
        _cartClicked.value = null
    }
}