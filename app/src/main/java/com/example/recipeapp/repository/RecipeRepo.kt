package com.example.recipeapp.repository
import com.example.recipeapp.Configs
import com.example.recipeapp.service.RecipeService


class RecipeRepo(private val recipeService: RecipeService) {

    suspend fun loadRecipes(term: String) = recipeService.getRecipes(
        Configs.TYPE,
        Configs.APP_ID,
        Configs.APP_KEY,
        term
    )

    suspend fun loadRecipe(id: String) = recipeService.getRecipe(
        recipeId = id,
        Configs.TYPE,
        Configs.APP_ID,
        Configs.APP_KEY,
    )
}