package com.example.recipeapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.db.AppDatabase
import com.example.recipeapp.db.Database
import com.example.recipeapp.db.RecipeEntity
import com.example.recipeapp.model.HitsData
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.repository.RecipeRepo
import kotlinx.coroutines.*
import java.lang.Exception


class RecipeViewModel(val recipeRepo: RecipeRepo) : ViewModel() {
    val recipeList = MutableLiveData<List<HitsData>>()
    val recipeObject = MutableLiveData<Recipe>()
    val loading = MutableLiveData<Boolean>()

    val errorMessage = MutableLiveData<String>()
    val completeMessage = MutableLiveData<String>()

    private var database: AppDatabase? = null
    private var job: Job? = null

    // ! Working with API
    fun getRecipes(term: String) {
        onCleared()
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(500L)
            if (term.length >= 3) {
                try {
                    loading.postValue(true)
                    val response = recipeRepo.loadRecipes(term)

                    if (response.isSuccessful) {
                        loading.postValue(false)

                        if (response.body()?.hits?.isEmpty() == true) {
                            onError("Такого Рецепта ещё не существует! 😥")
                            onCleared()
                        }
                        recipeList.postValue(response.body()?.hits)
                    }
                } catch (e: Exception) {
                    onError(e.toString())
                }
            }
        }
    }
    fun getRecipe(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                loading.postValue(true)
                val response = recipeRepo.loadRecipe(id)

                if (response.isSuccessful) {
                    onComplete("Получили данные из Сети")
                    recipeObject.postValue(response.body()?.recipe)
                    saveToDB(response.body()?.recipe!!)
                }
            } catch (e: Exception) {
                onError(e.toString())
            }
        }
    }

    // ! Working with DB
    fun createDB(context: Context) {
        database = Database.build(context)
    }
    fun saveToDB(recipe: Recipe) {
        val recipeEntity = RecipeEntity(
            image = recipe.image,
            calories = recipe.calories,
            ingredientLines = recipe.ingredientLines?.joinToString(","),
            label = recipe.label,
            recipeId = recipe.recipeId,
            yield = recipe.yield,
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database?.recipeDao()?.insertRecipe(recipeEntity)
            } catch (e: Exception) {
                onError(e.toString())
            }
        }
    }
    fun getDataFromDB(recipeID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val entityObject = database?.recipeDao()?.getRecipe(recipeID)

                if (entityObject != null) {
                    onComplete("Получили данные из СУБД")
                    recipeObject.postValue(entityObject.let {
                        Recipe(
                            label = it.label,
                            image = it.image,
                            calories = it.calories,
                            recipeId = it.recipeId,
                            yield = it.yield,
                            ingredientLines = it.ingredientLines?.split(",")
                        )
                    })
                } else {
                    // facepalm, but ...
                    recipeObject.postValue(null)
                }
            } catch (e: Exception) {
                onError(e.toString())
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }
    private fun onComplete(message: String) {
        completeMessage.postValue(message)
        loading.postValue(false)
    }
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}