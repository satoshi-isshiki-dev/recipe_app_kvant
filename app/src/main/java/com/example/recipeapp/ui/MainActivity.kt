package com.example.recipeapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.adapter.RecipeAdapter
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.repository.RecipeRepo
import com.example.recipeapp.service.RecipeService
import com.example.recipeapp.viewmodel.CViewModelFactory
import com.example.recipeapp.viewmodel.RecipeViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: RecipeViewModel
    private val adapter = RecipeAdapter()
    lateinit var binding: ActivityMainBinding

    private var launchSomeActivity: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.adapter = adapter

        viewModel = ViewModelProvider(
            this, CViewModelFactory(RecipeRepo(RecipeService.instance))
        )[RecipeViewModel::class.java]

        initObserves()
        viewModel.getRecipes("apple pie")

        binding.searchRecipeEditText.addTextChangedListener {
            viewModel.getRecipes(it.toString().trim())
        }
        launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}
        adapter.onItemClick = {
            val msgIntent = Intent(this, DetailActivity::class.java)
            msgIntent.putExtra("RECIPE_ID", it.recipeId)

            launchSomeActivity?.launch(msgIntent)
        }
    }

    private fun initObserves() {
        viewModel.recipeList.observe(this) {
            adapter.setRecipes(it)
        }
        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        viewModel.loading.observe(this) {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        }
    }
}