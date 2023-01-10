package com.cs683.culinarian.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.cs683.culinarian.CulinarianApplication
import com.cs683.culinarian.datalayer.RecipesWithFolders
import com.cs683.culinarian.model.Folder
import com.cs683.culinarian.model.Recipe
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RecipeFolderViewModel(application: Application) : AndroidViewModel(application) {
    val repository = (application as CulinarianApplication).repository
    val recipeId = MutableLiveData(0L)
    var recipeFolders: MutableLiveData<RecipesWithFolders> = MutableLiveData()
    var allFolders: MutableLiveData<MutableList<Folder>> = MutableLiveData()

    private val _relationsModified = MutableLiveData(false)
    val relationsModified: MutableLiveData<Boolean> get() = _relationsModified

    private val _navigateToRecipe = MutableLiveData<Boolean?>()
    val navigateToRecipe: MutableLiveData<Boolean?> get() = _navigateToRecipe

    fun okClicked() {
        _navigateToRecipe.value = true
    }

    fun onRecipeNavigated() {
        _navigateToRecipe.value = null
    }

    fun setRecipe(id: Long) {
        recipeId.value = id
        val relations = viewModelScope.async {
            repository.getFoldersByRecipeId(id)
        }
        val folders = viewModelScope.async {
            repository.getAllFoldersAsync()
        }
        viewModelScope.launch {
            val rels = relations.await()
            val fols = folders.await()
            val filteredFolders = fols.filter { it !in rels.folder }
            recipeFolders.value = rels
            allFolders.value = filteredFolders.toMutableList()
            _relationsModified.value = true
        }
    }

    fun removeFromFolder(folder: Folder) {
        recipeFolders.value!!.folder.remove(folder)
        allFolders.value!!.add(folder)
        viewModelScope.launch {
            repository.deleteMapping(folder.folderId, recipeId.value!!)
            _relationsModified.value = true
        }
    }

    fun addToFolder(folder: Folder) {
        allFolders.value!!.remove(folder)
        recipeFolders.value!!.folder.add(folder)
        viewModelScope.launch {
            repository.insertMapping(folder.folderId, recipeId.value!!)
            _relationsModified.value = true
        }
    }

    fun onRelationsUpdated() {
        _relationsModified.value = false
    }
}