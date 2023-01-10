package com.cs683.culinarian

import androidx.lifecycle.LiveData
import com.cs683.culinarian.datalayer.*
import com.cs683.culinarian.model.*
import java.util.concurrent.Executors

class Repository (
    private val folderDao: FolderDao,
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val instructionDao: InstructionDao,
    private val mappingTableDao: MappingTableDao) {

    val executor = Executors.newSingleThreadExecutor()

    fun searchDatabase(searhQuery: String): LiveData<List<Recipe>> {
        return recipeDao.searchDatabase(searhQuery)
    }

    // Folders
    fun addFolder(folder: Folder) {
        executor.execute{
            folderDao.addFolder(folder)
        }
    }

    fun deleteFolder(folder: Folder) {
        executor.execute{
            folderDao.deleteFolder(folder)
        }
    }

    fun editFolder(folder: Folder) {
        executor.execute{
            folderDao.editFolder(folder)
        }
    }

    fun getAllFolders():LiveData<List<Folder>> {
        return folderDao.getAllFolders()
    }

    suspend fun getAllFoldersAsync(): List<Folder> {
        return folderDao.getAllFoldersAsync()
    }

    //Recipe Folder Relations
    fun getRecipesByFolderId(folderId: Int): LiveData<FolderWithRecipes> {
        return mappingTableDao.getFolderWithRecipes(folderId)
    }

    suspend fun getFoldersByRecipeId(recipeId: Long): RecipesWithFolders {
        return mappingTableDao.getRecipesWithFolders(recipeId)
    }

    suspend fun addRecipesToFolder(folderId: Int, recipes: List<Recipe>) {
        val mappings: MutableList<MappingTable> = mutableListOf()
        for (recipe in recipes) mappings.add(MappingTable(folderId, recipe.recipeId))
        mappingTableDao.insertMappings(mappings)
    }

    suspend fun deleteRecipeFromFolder(folderId: Int, recipeId: Long) {
        mappingTableDao.deleteMapping(MappingTable(folderId, recipeId))
    }

    suspend fun insertMapping(folderId: Int, recipeId: Long) {
        mappingTableDao.insert(MappingTable(folderId, recipeId))
    }

    suspend fun deleteMapping(folderId: Int, recipeId: Long) {
        mappingTableDao.deleteMapping(MappingTable(folderId, recipeId))
    }

    // Recipes

    fun getAllRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

    suspend fun addRecipe(recipe: FullRecipe) : Long {
        val recipeId = recipeDao.addRecipe(recipe.recipe)
        for (ingredient in recipe.ingredients) {
            ingredient.recipeOwnerId = recipeId
        }
        for (instruction in recipe.instructions) {
            instruction.recipeOwnerId = recipeId
        }
        ingredientDao.addIngredients(recipe.ingredients)
        instructionDao.addInstructions(recipe.instructions)
        return recipeId
    }

    suspend fun delRecipe(recipe: FullRecipe) {
        recipeDao.delRecipe(recipe.recipe)
        ingredientDao.deleteIngredientList(recipe.ingredients)
        instructionDao.deleteInstructionList(recipe.instructions)
    }

    suspend fun delRecipe(recipe: Recipe) {
        recipeDao.delRecipe(recipe)
        ingredientDao.deleteIngredientByRecipeId(recipe.recipeId)
        instructionDao.deleteInstructionByRecipeId(recipe.recipeId)
    }

    //TODO: tag as transaction, also check DAOs
    suspend fun editRecipe(
        recipe: FullRecipe,
        newIngredients: List<Ingredient>,
        newInstructions: List<Instruction>,
        deletedIngredients: List<Ingredient>,
        deletedInstructions: List<Instruction>
    ) {
        ingredientDao.addIngredients(newIngredients)
        instructionDao.addInstructions(newInstructions)

        ingredientDao.deleteIngredientList(deletedIngredients)
        instructionDao.deleteInstructionList(deletedInstructions)

        recipeDao.editRecipe(recipe.recipe)
        ingredientDao.editIngredientList(recipe.ingredients)
        instructionDao.editInstructionList(recipe.instructions)
    }

    suspend fun editSingleRecipe(recipe: Recipe) {
        recipeDao.editRecipe(recipe)
    }

    fun getAllRecipes(recipeName: Long):LiveData<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

    fun getRecipe(recipeId: Long): LiveData<FullRecipe> {
        return recipeDao.getRecipeById(recipeId)
    }

//    Ingredients
    suspend fun addIngredients(ingredient: Ingredient) {
        ingredientDao.addIngredient(ingredient)
    }

    suspend fun delIngredient(ingredient: Ingredient) {
        ingredientDao.deleteIngredient(ingredient)
    }

    suspend fun editIngredient(ingredient: Ingredient) {
        ingredientDao.editIngredient(ingredient)
    }

    suspend fun editIngredientList(ingredients: List<Ingredient>) {
        ingredientDao.editIngredientList(ingredients)
    }

    fun getAllIngredients(): LiveData<List<Ingredient>> {
        return ingredientDao.getAllIngredients()
    }

    fun getShoppingList(): LiveData<List<Ingredient>> {
        return ingredientDao.getIngredientsInShoppingList()
    }

    //    Instruction
    suspend fun addInstruction(instruction: Instruction) {
        instructionDao.addInstruction(instruction)
    }

    suspend fun delInstruction(instruction: Instruction) {
        instructionDao.deleteInstruction(instruction)
    }

    suspend fun editInstruction(instruction: Instruction) {
        instructionDao.editInstruction(instruction)
    }

    fun getAllInstruction(): LiveData<List<Instruction>> {
        return instructionDao.getAllInstructions()
    }

    // Search Functionality

}
