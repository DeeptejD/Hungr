package com.example.recipeapp

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import com.example.recipeapp.data.model.Recipe
import com.example.recipeapp.repository.RecipeRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class RecipeRepositoryImplTest {
    private lateinit var context: Context
    private lateinit var repository: RecipeRepositoryImpl
    private lateinit var assetManager: AssetManager
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        assetManager = mock(AssetManager::class.java)
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)



        // Stub assets method from context to return the mock asset manager
        `when`(context.assets).thenReturn(assetManager)
        `when`(context.getSharedPreferences("SavedRecipePrefs", Context.MODE_PRIVATE)).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(sharedPreferences.getBoolean("SAVED_RECIPE_1", false)).thenReturn(false)
        `when`(sharedPreferences.getBoolean("SAVED_RECIPE_2", false)).thenReturn(true)

        val json = """
        [
            {
                "id": 1,
                "name": "Delicious Pasta",
                "category": "Main Course",
                "isVegetarian": false,
                "ingredients": ["Pasta", "Tomato Sauce", "Cheese"],
                "instructions": "Boil pasta. Mix with sauce. Add cheese and serve.",
                "isSaved": false,
                "imageResId": "pasta_image",
                "description": "A classic pasta dish with rich tomato sauce.",
                "cookingTime": "20 minutes"
            },
            {
                "id": 2,
                "name": "Crunchy Stir Fry",
                "category": "Snack",
                "isVegetarian": true,
                "ingredients": ["Bell Peppers", "Carrots", "Soy Sauce"],
                "instructions": "Stir-fry vegetables. Add sauce and serve with rice.",
                "isSaved": true,
                "imageResId": "stir_fry_image",
                "description": "A quick and tasty vegetable stir-fry.",
                "cookingTime": "15 minutes"
            }
        ]
        """

        `when`(assetManager.open("recipes.json")).thenReturn(json.byteInputStream())

        repository = RecipeRepositoryImpl(context)
    }

    /**
     * Tests if fetching all recipes actually fetches the recipes that match the JSON
     */
    @Test
    fun testGetAllRecipes() = runTest {
        val expectedRecipes = listOf(
            Recipe(
                id = 1,
                name = "Delicious Pasta",
                category = "Main Course",
                isVegetarian = false,
                ingredients = listOf("Pasta", "Tomato Sauce", "Cheese"),
                instructions = "Boil pasta. Mix with sauce. Add cheese and serve.",
                isSaved = false,
                imageResId = "pasta_image",
                description = "A classic pasta dish with rich tomato sauce.",
                cookingTime = "20 minutes"
            ),
            Recipe(
                id = 2,
                name = "Crunchy Stir Fry",
                category = "Snack",
                isVegetarian = true,
                ingredients = listOf("Bell Peppers", "Carrots", "Soy Sauce"),
                instructions = "Stir-fry vegetables. Add sauce and serve with rice.",
                isSaved = true,
                imageResId = "stir_fry_image",
                description = "A quick and tasty vegetable stir-fry.",
                cookingTime = "15 minutes"
            )
        )
        val recipesFlow = repository.getAllRecipes()
        val recipes = recipesFlow.first()
        assertEquals(expectedRecipes, recipes)
    }

    /**
     * Tests if fetching a recipe provided its ID works
     */
    @Test
    fun testGetRecipeById() = runTest {
        val expectedRecipe = Recipe(
            id = 1,
            name = "Delicious Pasta",
            category = "Main Course",
            isVegetarian = false,
            ingredients = listOf("Pasta", "Tomato Sauce", "Cheese"),
            instructions = "Boil pasta. Mix with sauce. Add cheese and serve.",
            isSaved = false,
            imageResId = "pasta_image",
            description = "A classic pasta dish with rich tomato sauce.",
            cookingTime = "20 minutes"
        )
        val recipeFlow = repository.getRecipeById(1)
        val recipe = recipeFlow.first()
        assertEquals(expectedRecipe, recipe)
    }

    /**
     * Tests if triggering the save function toggles the recipe save state
     */
    @Test
    fun testSaveRecipe() {
        val recipe = Recipe(
            id = 1,
            name = "Delicious Pasta",
            category = "Main Course",
            isVegetarian = false,
            ingredients = listOf("Pasta", "Tomato Sauce", "Cheese"),
            instructions = "Boil pasta. Mix with sauce. Add cheese and serve.",
            isSaved = false,
            imageResId = "pasta_image",
            description = "A classic pasta dish with rich tomato sauce.",
            cookingTime = "20 minutes"
        )
        repository.saveRecipe(recipe)
        assertEquals(true, recipe.isSaved)
        repository.saveRecipe(recipe)
        assertEquals(false, recipe.isSaved)
    }
}