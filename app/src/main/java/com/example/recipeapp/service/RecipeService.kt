package com.example.recipeapp.service

import com.example.recipeapp.Configs
import com.example.recipeapp.model.HitsData
import com.example.recipeapp.model.RecipeResponseData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RecipeService {

    @GET("api/recipes/v2")
    suspend fun getRecipes(
        @Query("type") type: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("q") query: String,
    ) : Response<RecipeResponseData>

    @GET("api/recipes/v2/{id}")
    suspend fun getRecipe(
        @Path("id") recipeId: String,
        @Query("type") type: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
    ) : Response<HitsData>

    companion object {
        val instance: RecipeService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(Configs.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(RecipeService::class.java)
        }
    }
}