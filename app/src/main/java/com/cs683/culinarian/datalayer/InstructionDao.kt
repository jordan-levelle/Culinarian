package com.cs683.culinarian.datalayer

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cs683.culinarian.model.Instruction

@Dao
interface InstructionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInstruction(instruction: Instruction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInstructions(instruction: List<Instruction>)

    @Update
    suspend fun editInstruction(instruction: Instruction)

    @Update
    suspend fun editInstructionList(instructions: List<Instruction>)

    @Delete
    suspend fun deleteInstruction(instruction: Instruction)

    @Delete
    suspend fun deleteInstructionList(instructions: List<Instruction>)

    @Query("DELETE FROM instruction WHERE recipeOwnerId = :recipeId")
    suspend fun deleteInstructionByRecipeId(recipeId: Long)

    @Query("SELECT * FROM instruction")
    fun getAllInstructions() : LiveData<List<Instruction>>

    @Query("SELECT * FROM instruction WHERE instructionId = :instructionId")
    fun getInstructionById(instructionId: Long) : LiveData<Instruction>
}