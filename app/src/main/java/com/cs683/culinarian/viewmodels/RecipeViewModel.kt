package com.cs683.culinarian.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.cs683.culinarian.CulinarianApplication
import com.cs683.culinarian.model.*
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    val repository = (application as CulinarianApplication).repository

    //when recipeId is updated _recipe retrieves new recipe from db,
    //when new recipe is returned to _recipe, recipe sorts/processes for display
    val recipeId = MutableLiveData(0L)
    val _recipe = Transformations.switchMap(recipeId) { id ->
        when(id) {
            0L -> MutableLiveData(FullRecipe(Recipe(0L, recipeName, 0), mutableListOf(), mutableListOf()))
            else -> repository.getRecipe(id)
        }
    }
    val recipe = Transformations.switchMap(_recipe) { newRecipe ->
        MutableLiveData(processRecipe(newRecipe))
    }

    private var recipeName = ""
    private var folderId = 0
    val ingredientInput = MutableLiveData("")
    val instructionInput = MutableLiveData("")

    private var addedIngredients = mutableListOf<Ingredient>()
    private var addedInstructions = mutableListOf<Instruction>()
    private var deletedInstructions = mutableListOf<Instruction>()
    private var deletedIngredients = mutableListOf<Ingredient>()

    private val _addedIngredient = MutableLiveData<Boolean?>()
    val addedIngredient: LiveData<Boolean?> get() = _addedIngredient

    private val _deletedIngredient = MutableLiveData<Int?>()
    val deletedIngredient: LiveData<Int?> get() = _deletedIngredient

    private val _addedInstruction = MutableLiveData<Boolean?>()
    val addedInstruction: LiveData<Boolean?> get() = _addedInstruction

    private val _deletedInstruciton = MutableLiveData<Int?>()
    val deletedInstruction: LiveData<Int?> get() = _deletedInstruciton

    private val _navigateToRecipe = MutableLiveData<Boolean>()
    val navigateToRecipe: LiveData<Boolean> get() = _navigateToRecipe

    private val _navigateToFolders = MutableLiveData<Boolean>()
    val navigateToFolders: LiveData<Boolean> get() = _navigateToFolders

    private val _submitRecipe = MutableLiveData<Boolean>(false)
    val submitRecipe: LiveData<Boolean> get() = _submitRecipe

    private val _submitToFolder = MutableLiveData<Boolean>(false)
    val submitToFolder: LiveData<Boolean> get() = _submitToFolder

    private val _cancelEdit = MutableLiveData<Boolean>(false)
    val cancelEdit: LiveData<Boolean> get() = _cancelEdit

    private val _addAllToCart = MutableLiveData<Boolean>(false)
    val addAllToCart: LiveData<Boolean> get() = _addAllToCart

    private val _navigateToRelations = MutableLiveData(false)
    val navigateToRelations: LiveData<Boolean> get() = _navigateToRelations

    fun setRecipe(id: Long) {
        recipeId.value = id
    }

    fun setRecipeWithFolder(newRecipeName: String, newFolderId: Int) {
        recipeName = newRecipeName
        folderId = newFolderId
        recipeId.value = 0L
    }

    fun processRecipe(newRecipe: FullRecipe): FullRecipe {
        newRecipe.ingredients.sortBy { it.position }
        newRecipe.instructions.sortBy { it.position }
        return newRecipe
    }

    fun ratingChanged(rating: Int) {
        viewModelScope.launch {
            recipe.value!!.recipe.recipeRating = rating
            repository.editSingleRecipe(recipe.value!!.recipe)
        }
    }

    fun deleteRecipe() {
        _navigateToFolders.value = true
        val recipe = recipe.value!!
        setRecipe(0L)
        viewModelScope.launch {
            repository.delRecipe(recipe)
        }
    }

    fun updateRecipe() {
        viewModelScope.launch {
            repository.editRecipe(recipe.value!!, addedIngredients, addedInstructions, deletedIngredients, deletedInstructions)
            resetEdits()
            _submitRecipe.value = false
        }
    }

    fun addRecipe() {
        viewModelScope.launch {
            val recipeId = repository.addRecipe(recipe.value!!)
            if (folderId != 0) repository.insertMapping(folderId, recipeId)
            setRecipe(recipeId)
            resetEdits()
            _submitRecipe.value = false
            _submitToFolder.value = false
        }
    }

    fun addAllToCart() {
        val ingredients = recipe.value!!.ingredients
        for (i in ingredients) {
            i.inShoppingList = true
        }
        viewModelScope.launch {
            repository.editIngredientList(ingredients)
        }
        _addAllToCart.value = true
    }

    fun shoppingCartClicked(position: Int, cartClicked: Boolean) {
        viewModelScope.launch {
            val ingredient = recipe.value!!.ingredients[position]
            ingredient.inShoppingList = cartClicked
            repository.editIngredient(ingredient)
        }
    }

    fun removeFromCart(recipe_ingredient: Pair<Long, Long>) {
        val recipeId = recipe_ingredient.first
        val ingredientId = recipe_ingredient.second

        if (recipeId == this.recipeId.value) {
            for (i in recipe.value!!.ingredients) {
                if (i.ingredientId == ingredientId) {
                    i.inShoppingList = false
                    break
                }
            }
        }
    }

    fun clearCart() {
        if (recipe.value != null) {
            for (ingredient in recipe.value!!.ingredients) {
                ingredient.inShoppingList = false
            }
        }
    }

    fun editClicked() {
        _navigateToRecipe.value = true
    }

    fun submitClicked() {
        if (folderId == 0)
            _submitRecipe.value = true
        else
            _submitToFolder.value = true
    }

    fun cancelClicked() {
        resetEdits()
        _cancelEdit.value = true
    }

    fun foldersClicked() {
        _navigateToRelations.value = true
    }

    fun onRelationsNavigated() {
        _navigateToRelations.value = false
    }

    fun onRecipeNavigated() {
        _navigateToRecipe.value = false
    }

    fun onNavigatedToFolders() {
        _navigateToFolders.value = false
    }

    fun updateCanceled() {
        _cancelEdit.value = false
    }

    fun addedToCart() {
        _addAllToCart.value = false
    }

    fun addIngredient() {
        if (ingredientInput.value != "") {
            val newIngredient = Ingredient(0L, recipe.value!!.recipe.recipeId,
                ingredientInput.value!!, 0.0, "", recipe.value!!.ingredients.size)
            addedIngredients.add(newIngredient)
            recipe.value!!.ingredients.add(newIngredient)
            ingredientInput.value = ""
            _addedIngredient.value = true
        }
    }

    fun updateIngredient(position: Int, amount: String) {
        val updatedIngredient = recipe.value!!.ingredients[position]
        updatedIngredient.ingredientAmount = when (amount) {
            "" -> 0.0
            else -> amount.toDouble()
        }
    }

    fun deleteIngredient(position: Int) {
        val ingredients = recipe.value!!.ingredients
        val delIngredient = ingredients[position]
        ingredients.removeAt(position)
        deletedIngredients.add(delIngredient)
        addedIngredients.remove(delIngredient)

        for (i in position until ingredients.size) {
            ingredients[i].position = i
        }

        _deletedIngredient.value = position
    }

    fun moveIngredient(from: Int, to: Int) {
        val ingredients = recipe.value!!.ingredients
        val draggedItem = ingredients[from]
        val targetItem = ingredients[to]
        ingredients.removeAt(from)
        ingredients.add(to, draggedItem)
        draggedItem.position = to
        targetItem.position = from
    }

    fun addedIngredient() {
        _addedIngredient.value = null
    }

    fun deletedIngredient() {
        _deletedIngredient.value = null
    }

    fun addInstruction() {
        if (instructionInput.value != "") {
            val newInstruction = Instruction(0L, recipe.value!!.recipe.recipeId,
                instructionInput.value!!, recipe.value!!.instructions.size)
            addedInstructions.add(newInstruction)
            recipe.value!!.instructions.add(newInstruction)
            instructionInput.value = ""
            _addedInstruction.value = true
        }
    }

    fun updateInstruction(position: Int, instruction: String) {
        val instructions = recipe.value!!.instructions
        if (instructions.isNotEmpty()) {
            instructions[position].instructionText = instruction
        }
    }

    fun deleteInstruction(position: Int) {
        val instructions = recipe.value!!.instructions
        val delInstruction = instructions[position]
        instructions.removeAt(position)
        deletedInstructions.add(delInstruction)
        addedInstructions.remove(delInstruction)

        for (i in position until instructions.size) {
            instructions[i].position = i
        }

        _deletedInstruciton.value = position
    }

    fun moveInstruction(from: Int, to: Int) {
        val instructions = recipe.value!!.instructions
        val draggedItem = instructions[from]
        val targetItem = instructions[to]
        instructions.removeAt(from)
        instructions.add(to, draggedItem)
        draggedItem.position = to
        targetItem.position = from
    }

    fun addedInstruction() {
        _addedInstruction.value = null
    }

    fun deletedInstruction() {
        _deletedIngredient.value = null
    }

    fun resetEdits() {
        addedIngredients.clear()
        deletedIngredients.clear()
        ingredientInput.value = ""
        addedInstructions.clear()
        deletedInstructions.clear()
        instructionInput.value = ""
        recipeName = ""
        folderId = 0
    }
}