package com.example.recipeapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter


@Entity(tableName = "Recipe")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val recipeId: String? = null,
    val label: String? = null,
    val image: String? = null,
    val calories: Float? = null,
    val yield: Float? = null,

    val ingredientLines: String? = null
    //    @TypeConverters(IngredientLinesConverter::class)
    //    val ingredientLines: List<String>? = null,
)

//class IngredientLinesConverter {
//    @TypeConverter
//    fun fromIngredientLines(ingredientLines: List<String>): String {
//        return ingredientLines.joinToString(",")
//    }
//    @TypeConverter
//    fun toIngredientLines(data: String): List<String> {
//        return data.split(",")
//    }
//}