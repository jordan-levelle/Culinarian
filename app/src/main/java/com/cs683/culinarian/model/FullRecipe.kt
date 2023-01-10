package com.cs683.culinarian.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FullRecipe(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipeOwnerId"
    )
    val instructions: MutableList<Instruction>,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipeOwnerId",
    )
    val ingredients: MutableList<Ingredient>
)