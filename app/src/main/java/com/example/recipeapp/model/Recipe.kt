package com.example.recipeapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Recipe(
    @SerializedName("uri")
    val recipeId: String? = null,

    val label: String? = null,
    val image: String? = null,
    val calories: Float? = null,
    val yield: Float? = null,
    val ingredientLines: List<String>? = null,
) : Serializable