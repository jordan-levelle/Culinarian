package com.cs683.culinarian.datalayer

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cs683.culinarian.model.Folder

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFolder(folder: Folder)

    @Update
    fun editFolder(folder: Folder)

    @Query("SELECT * FROM folders")
    fun getAllFolders(): LiveData<List<Folder>>

    @Query("SELECT * FROM folders")
    suspend fun getAllFoldersAsync(): List<Folder>

    @Delete
    fun deleteFolder(folder: Folder)

    @Transaction
    @Query("SELECT * FROM folders WHERE folderId = :folderId")
    fun getFolderById(folderId : Int): LiveData<Folder>
}