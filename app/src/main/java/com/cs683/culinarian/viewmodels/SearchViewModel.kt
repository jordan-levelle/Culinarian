package com.cs683.culinarian.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.cs683.culinarian.CulinarianApplication
import com.cs683.culinarian.model.Recipe


class SearchViewModel (application: Application) : AndroidViewModel(application) {

    private val repository = (application as CulinarianApplication).repository

    val searchQuery = MutableLiveData("")

    private val recipesLiveData: LiveData<List<Recipe>> = Transformations.switchMap(searchQuery) {
        repository.searchDatabase(it)
    }

    val recipes = Transformations.switchMap(recipesLiveData) {
        MutableLiveData(processRecipes(it))
    }


    val recipesUncheck = Transformations.switchMap(recipesLiveData) {
        MutableLiveData(processrecipesUncheck(it))
    }

    private fun processrecipesUncheck(it: List<Recipe>?) {

    }

    /*
    * Checked Recipes
    */
    var recipeChecked = MutableLiveData<Recipe?>()
    private val checkRecipes = mutableListOf<Recipe>()

    fun recipeCheckClicked( pos: Int ) {
        val recipe = recipes.value!![pos]
        recipes.value!!.removeAt(pos)
        checkRecipes.add(recipe)
        recipeChecked.value = recipe
    }

    private fun processRecipes(recipesList: List<Recipe> ) : MutableList<Recipe> {
        val movedRecipes: MutableList<Recipe> = mutableListOf()
        for (recipe in recipesList) {
            movedRecipes.add(recipe)
        }
        return movedRecipes
    }

    fun checkComplete() {
        recipeChecked.value = null
    }

    /*
    * UnChecked Recipes
    */

    var recipeUnchecked = MutableLiveData<Recipe?>()
    private val uncheckRecipe = mutableListOf<Recipe>()


    fun recipeUncheckClicked(pos: Int) {
        val recipe = recipes.value!![pos]
        recipes.value!!.removeAt(pos)
        uncheckRecipe.add(recipe)
        recipeUnchecked.value = recipe
    }

    fun uncheckComplete() {
        recipeUnchecked.value = null
    }
}