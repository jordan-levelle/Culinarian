package com.cs683.culinarian.datalayer

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cs683.culinarian.model.FullRecipe
import com.cs683.culinarian.model.Recipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipe(recipe: Recipe) : Long

    @Update
    suspend fun editRecipe(recipe: Recipe)

    @Delete
    suspend fun delRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Transaction
    @Query("SELECT * FROM recipe WHERE recipeId = :recipeId")
    fun getRecipeById(recipeId: Long): LiveData<FullRecipe>


    // This query allows for the string to be in any part of the recipeName
    @Query("SELECT * FROM recipe WHERE recipeName LIKE '%' || :searchQuery || '%'")
    fun searchDatabase(searchQuery: String) : LiveData<List<Recipe>>
}