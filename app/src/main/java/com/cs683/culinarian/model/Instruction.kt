package com.cs683.culinarian.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "instruction")
data class Instruction(
    @PrimaryKey(autoGenerate = true)
    var instructionId: Long = 0L,
    var recipeOwnerId: Long = 0L,
    var instructionText: String = "",
    var position: Int = 0
)