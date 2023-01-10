package com.cs683.culinarian.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    var ingredientId: Long = 0L,
    var recipeOwnerId: Long = 0L,
    var ingredientName: String = "",
    var ingredientAmount: Double = 0.0,
    var ingredientUnit: String = "",
    var position: Int = 0,
    var inShoppingList: Boolean = false
)