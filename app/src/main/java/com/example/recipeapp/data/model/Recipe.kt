package com.example.recipeapp.data.model

import androidx.annotation.DrawableRes

data class Recipe(
    val id: Int,
    val name: String,
    val category: String,
    val isVegetarian: Boolean,
    val ingredients: List<String>,
    val instructions: String,
    var isSaved: Boolean = false,
    val imageResId: String,
    val description: String,
    val cookingTime: String
)
