package com.cs683.culinarian.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    var recipeId: Long = 0L,
    var recipeName: String = "",
    var recipeRating: Int = 0
)