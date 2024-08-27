package com.example.recipeapp.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.model.Recipe
import com.example.recipeapp.repository.RecipeRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepositoryImpl) : ViewModel() {
    // StateFlow because it will always hold the latest emitted val
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())

    // The following three properties are used to filter the recipes that are displayed

    // Nullable -> Null == All
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Nullable -> Null == All
    private val _isVegetarianFilter = MutableStateFlow<Boolean?>(null)
    val isVegetarianFilter: StateFlow<Boolean?> = _isVegetarianFilter

    // combine emits a new value when any of the src flows emit a new val
    @OptIn(ExperimentalCoroutinesApi::class)
    val recipes: Flow<List<Recipe>> = combine(
        _selectedCategory,
        _searchQuery,
        _isVegetarianFilter
    ) { category, query, isVegetarian ->
        Triple(category, query, isVegetarian)
    }.flatMapLatest { (category, query, isVegetarian) ->
        repository.getAllRecipes().map { list ->
            list.filter { recipe ->
                val queryWords = query.split(" ").filter { it.isNotBlank() }
                (category == null || recipe.category == category) &&
                (query.isBlank() || queryWords.any { recipe.name.contains(it, ignoreCase = true) }) &&
                (isVegetarian == null || recipe.isVegetarian == isVegetarian)
            }
        }
    }

    init {
        loadRecipes()
    }

    // This emits a new value to the category, which triggers the above upgrade of recipes
    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
    }

    // triggers an update of the recipes by emitting a new qry val
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // This emits a new val to isVeg which triggers the recipe update
    fun onVegetarianFilterSelected(isVegetarian: Boolean?) {
        _isVegetarianFilter.value = isVegetarian
    }

    // getAllRecipes reads the recipes from JSON and emits them
    @VisibleForTesting
    fun loadRecipes() {
        viewModelScope.launch {
            repository.getAllRecipes().collect { recipes ->
                _recipes.value = recipes
            }
        }
    }

    // Launches the saveRecipe function from the repo
    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.saveRecipe(recipe)
        }
    }

    fun getRecipeById(recipeId: Int): Flow<Recipe?> {
        return recipes.map { list -> list.find { it.id == recipeId } }
    }
}
