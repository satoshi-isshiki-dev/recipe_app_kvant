package com.example.recipeapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Query("SELECT * FROM Recipe WHERE recipeId = :newRecipeId")
    suspend fun getRecipe(newRecipeId: String): RecipeEntity
}