package com.example.recipeapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.ActivityDetailBinding
import com.example.recipeapp.makeToast
import com.example.recipeapp.repository.RecipeRepo
import com.example.recipeapp.service.RecipeService
import com.example.recipeapp.viewmodel.CViewModelFactory
import com.example.recipeapp.viewmodel.RecipeViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    lateinit var viewModel: RecipeViewModel
    lateinit var id: String
    lateinit var recipeID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Подробно про рецепт"

        viewModel = ViewModelProvider(
            this, CViewModelFactory(RecipeRepo(RecipeService.instance))
        )[RecipeViewModel::class.java]

        initDB()
        initObserves()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun initDB() {
        // ! В виду отсутсвия у API отдельного поля для ID
        // ! Да будет костыль! ...
        id = intent.getStringExtra("RECIPE_ID").toString()

        recipeID = id.substring(
            id.indexOf('#', 0)+1,
            id.length
        )

        viewModel.createDB(this)
        viewModel.getDataFromDB(id ?: "")
    }

    private fun initObserves() {
        viewModel.recipeObject.observe(this) {
            if (it == null) {
                viewModel.getRecipe(recipeID ?: "")
            } else {
                Glide.with(this).load(it.image).into(binding.recipeImg)
                binding.titleTextView.text = "Название рецепта: ${it.label}"
                binding.caloriesTextView.text = "Калории рецепта: ${it.calories}"
                binding.ratingTextView.text = "Рейтинг: ${it.yield}"
                binding.ingredientsTextView.text = it.ingredientLines?.reduce { acc, s -> acc + "\n" + s }

                supportActionBar?.title = "${it.label}"
            }
        }
        viewModel.errorMessage.observe(this) {
            makeToast(it, this)
        }
        viewModel.completeMessage.observe(this) {
            makeToast(it, this)
        }
        viewModel.loading.observe(this) {
            if (it) {
                binding.progressBarEdit.visibility = View.VISIBLE
            } else {
                binding.progressBarEdit.visibility = View.GONE
            }
        }
    }
}