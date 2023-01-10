package com.cs683.culinarian.datalayer

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cs683.culinarian.model.Folder
import com.cs683.culinarian.model.Ingredient
import com.cs683.culinarian.model.Instruction
import com.cs683.culinarian.model.Recipe

@Database(
    entities = [Folder::class, Recipe::class, Ingredient::class, Instruction::class, MappingTable::class],
    version = 1
)
abstract class CulinarianDatabase: RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun instructionDao(): InstructionDao
    abstract fun mappingTableDao(): MappingTableDao
}