package com.example.recipeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.RecipeItemBinding
import com.example.recipeapp.model.Recipe
import com.bumptech.glide.Glide
import com.example.recipeapp.model.HitsData

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeHolder>() {

    private var recipeList = mutableListOf<HitsData>()

    var onItemClick : ((Recipe) -> Unit)? = null

    class RecipeHolder(val binding: RecipeItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setRecipes(recipes: List<HitsData>) {
        recipeList = recipes.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeItemBinding.inflate(inflater, parent, false)

        return RecipeHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val recipe = recipeList[position]
        holder.binding.name.text = recipe.recipe?.label
        Glide.with(holder.itemView.context).load(recipe.recipe?.image).into(holder.binding.imageview)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recipe.recipe ?: Recipe(label = "", image = ""))
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}