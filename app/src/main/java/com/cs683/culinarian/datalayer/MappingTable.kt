package com.cs683.culinarian.datalayer

import androidx.room.*
import com.cs683.culinarian.model.Folder
import com.cs683.culinarian.model.Recipe



// Third Class that represents the associative entity. Cross Reference table
@Entity(primaryKeys = ["folderId", "recipeId"])
data class MappingTable(
    val folderId: Int,
    val recipeId: Long
)

// Table with single folder object with list of all recipes that the folder includes
data class FolderWithRecipes (
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "recipeId",
        associateBy = Junction(MappingTable::class)
    )
    val recipe: MutableList<Recipe>
)

// Table with single recipe object with list of all folders a recipe is included in
data class RecipesWithFolders (
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "folderId",
        associateBy = Junction(MappingTable::class)
    )
    val folder: MutableList<Folder>
)