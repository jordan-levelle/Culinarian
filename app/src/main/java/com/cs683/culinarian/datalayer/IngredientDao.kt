package com.cs683.culinarian.datalayer

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cs683.culinarian.model.Ingredient

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIngredient(ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIngredients(ingredients: List<Ingredient>)

    @Update
    suspend fun editIngredient(ingredient: Ingredient)

    @Update
    suspend fun editIngredientList(ingredients: List<Ingredient>)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredientList(ingredient: List<Ingredient>)

    @Query("DELETE FROM ingredient WHERE recipeOwnerId = :recipeId")
    suspend fun deleteIngredientByRecipeId(recipeId: Long)

    @Query("SELECT * FROM ingredient")
    fun getAllIngredients() : LiveData<List<Ingredient>>

    @Query("SELECT * FROM ingredient WHERE ingredientName = :ingredientName")
    fun getIngredientByName(ingredientName: String) : LiveData<Ingredient>

    @Query("SELECT * FROM ingredient WHERE inShoppingList = 1 ORDER BY ingredientName, ingredientAmount")
    fun getIngredientsInShoppingList() : LiveData<List<Ingredient>>
}
