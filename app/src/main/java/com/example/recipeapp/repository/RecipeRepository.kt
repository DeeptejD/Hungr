package com.example.recipeapp.repository

import com.example.recipeapp.data.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    // fetches all the recipes
    fun getAllRecipes(): Flow<List<Recipe>>

    // fetches a recipe by id
    fun getRecipeById(id: Int): Flow<Recipe>

    // saves a recipe to the library
    fun saveRecipe(recipe: Recipe)
}