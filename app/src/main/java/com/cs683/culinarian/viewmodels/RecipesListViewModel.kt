package com.cs683.culinarian.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.cs683.culinarian.CulinarianApplication
import com.cs683.culinarian.datalayer.FolderWithRecipes
import com.cs683.culinarian.model.Folder
import com.cs683.culinarian.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(application : Application): AndroidViewModel(application) {
    private val repository = (application as CulinarianApplication).repository

    val folderId = MutableLiveData( 0)
    private var addFolderId = 0
    private val _navigateToFolder = MutableLiveData<Int?>()
    val navigateToFolder : LiveData<Int?> get() = _navigateToFolder

    val _folder = Transformations.switchMap(folderId){ id ->
        when(id) {
            0 -> MutableLiveData(FolderWithRecipes(Folder(0,"All Recipes"), mutableListOf()))
            else -> repository.getRecipesByFolderId(id)
        }
    }

    val _recipes = Transformations.switchMap(_folder){ folder ->
        when(folder.folder.folderId){
            0 -> repository.getAllRecipes()
            else -> MutableLiveData(folder.recipe)
        }
    }

    fun setAddFolderId(folderId: Int){
        addFolderId = folderId
    }

    /**
     * Map correct recipes to correct folderId
     */
    fun setCurFolder(newId: Int){
        folderId.value = newId
    }

    /**
    * List of added recipes for Folder
    */
    private var addedRecipes = mutableListOf<Recipe>()


    /**
     * Add Recipes To folder
     */
    fun addCheckedRecipes(position: Int) {
        val recipe = _recipes.value!![position]
        if (recipe in addedRecipes) {
            addedRecipes.remove(recipe)
        } else {
            addedRecipes.add(recipe)
        }
    }

    fun submitRecipes(){
        viewModelScope.launch {
            repository.addRecipesToFolder(addFolderId, addedRecipes)
        }
        _navigateToFolder.value = addFolderId
    }

    fun delRecipeFromFolder(recipe: Recipe){
        viewModelScope.launch {
            if (folderId.value!! == 0) {
                repository.delRecipe(recipe)
            }
            repository.deleteRecipeFromFolder(folderId.value!!, recipe.recipeId)
        }
    }
}
