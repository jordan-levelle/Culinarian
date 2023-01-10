package com.cs683.culinarian.datalayer

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MappingTableDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(join: MappingTable)

    // Insert recipe mapped to folder
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMappings(join: List<MappingTable>)

    // Delete recipe mapped to folder
    @Delete
    suspend fun deleteMapping(join: MappingTable)

    // Queries database and returns all recipes for a folder
    @Transaction
    @Query("SELECT * FROM folders WHERE folderId = :folderId")
    fun getFolderWithRecipes(folderId: Int): LiveData<FolderWithRecipes>

    // Queries database and returns all folders recipe is included in
    @Transaction
    @Query("SELECT * FROM recipe WHERE recipeId = :recipeId")
    suspend fun getRecipesWithFolders(recipeId: Long): RecipesWithFolders
}
