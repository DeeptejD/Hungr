package com.example.recipeapp.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.recipeapp.data.model.Recipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipeRepositoryImpl(private val context: Context) : RecipeRepository {
    private val prefs = context.getSharedPreferences("SavedRecipePrefs", Context.MODE_PRIVATE)

    private fun loadRecipesFromAssets(): List<Recipe> {
        val json = context.assets.open("recipes.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Recipe>>() {}.type
        return Gson().fromJson(json, type)
    }

    // Generates a cold flow
    override fun getAllRecipes(): Flow<List<Recipe>> = flow {
        val recipes = loadRecipesFromAssets()
        val updatedRecipes = recipes.map { recipe ->
            val isSaved = prefs.getBoolean("SAVED_RECIPE_${recipe.id}", false)
            recipe.copy(isSaved = isSaved)
        }
        emit(updatedRecipes)
    }

    override fun getRecipeById(id: Int): Flow<Recipe> = flow {
        val recipes = loadRecipesFromAssets()  // Reload recipes from JSON
        val recipe = recipes.first { it.id == id }
        val isSaved = prefs.getBoolean("SAVED_RECIPE_${recipe.id}", false)
        emit(recipe.copy(isSaved = isSaved))
    }

    // This toggles because of the way I implemented the favorite button
    override fun saveRecipe(recipe: Recipe) {
        recipe.isSaved = !recipe.isSaved
        val editor = prefs.edit()
        editor.putBoolean("SAVED_RECIPE_${recipe.id}", recipe.isSaved)
        editor.apply()
//        sharedPreferences.edit().putBoolean(recipe.id.toString(), recipe.isSaved).apply()
    }
}
