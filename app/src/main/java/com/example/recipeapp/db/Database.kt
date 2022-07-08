package com.example.recipeapp.db

import android.content.Context
import androidx.room.*
import androidx.room.Database


@Database(
    entities = [RecipeEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}

object Database {
    fun build(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()
}