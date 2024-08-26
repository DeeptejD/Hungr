package com.example.recipeapp.repository

import android.content.Context
import com.example.recipeapp.data.model.Recipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipeRepositoryImpl(private val context: Context) : RecipeRepository {
    // Requires context to be able to open the json from assets folder
    private val recipes: List<Recipe> by lazy {
        val json = context.assets.open("recipes.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Recipe>>() {}.type
        Gson().fromJson(json, type)
    }

    // Generates a cold flow
    override fun getAllRecipes(): Flow<List<Recipe>> = flow { emit(recipes) }

    override fun getRecipeById(id: Int): Flow<Recipe> = flow {
        emit(recipes.first { it.id == id })
    }

    // This toggles because of the way I implemented the favorite button
    override fun saveRecipe(recipe: Recipe) {
        recipe.isSaved = !recipe.isSaved
    }
}
