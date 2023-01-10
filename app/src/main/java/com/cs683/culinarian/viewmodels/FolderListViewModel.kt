package com.cs683.culinarian.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cs683.culinarian.CulinarianApplication
import com.cs683.culinarian.model.Folder
import kotlinx.coroutines.launch

class FolderListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as CulinarianApplication).repository

    private val _folderList: LiveData<List<Folder>> = repository.getAllFolders()
    val folderList:LiveData<List<Folder>> get() = _folderList

    /**
     * Add a Folder
     */
    fun addFolder(folder: Folder) {
        repository.addFolder(folder)
    }

    /**
     * Swipe to delete Folder
     */
    fun deleteFolder(folder: Folder) = viewModelScope.launch {
        repository.deleteFolder(folder)
    }
}