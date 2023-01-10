package com.cs683.culinarian

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cs683.culinarian.datalayer.CulinarianDatabase
import com.cs683.culinarian.model.Folder
import com.cs683.culinarian.model.Ingredient
import com.cs683.culinarian.model.Instruction
import com.cs683.culinarian.model.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class CulinarianApplication: Application() {
    lateinit var database: CulinarianDatabase
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()

        database =
            Room.databaseBuilder(
            applicationContext, CulinarianDatabase::class.java,
            "Database-db"
        ).build()

        repository = Repository(
            database.folderDao(),
            database.recipeDao(),
            database.ingredientDao(),
            database.instructionDao(),
            database.mappingTableDao()
        )
    }
}
