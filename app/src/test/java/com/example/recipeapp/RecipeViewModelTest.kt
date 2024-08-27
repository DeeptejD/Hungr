package com.example.recipeapp

import com.example.recipeapp.data.model.Recipe
import com.example.recipeapp.repository.RecipeRepositoryImpl
import com.example.recipeapp.viewmodel.RecipeViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doNothing

@RunWith(JUnit4::class)
class RecipeViewModelTest {
    private var repository: RecipeRepositoryImpl = mockk(relaxed = true)
    private lateinit var viewModel: RecipeViewModel
    private val testDispatcher = StandardTestDispatcher() // This is the dispatcher that test coroutines will use
    val recipes = listOf(
        Recipe(1, "Spaghetti Bolognese", "Main Course", false, listOf("Spaghetti", "Ground Beef", "Tomato Sauce"), "Cook spaghetti. Prepare sauce. Combine and serve.", false, "", "", ""),
        Recipe(2, "Vegetable Stir Fry", "Snack", true, listOf("Broccoli", "Bell Peppers", "Soy Sauce"), "Stir fry vegetables. Add sauce. Serve with rice.", true, "", "", "")
    )
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RecipeViewModel(repository)
        // stubbing the getAllRecipes method to return a predefined list of recipes
        every { repository.getAllRecipes() } returns flowOf(recipes)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * This function tests whether the loadRecipes function correctly loads the recipes inside viewModel.recipes after calling getAllRecipes on the repository
     */
    @Test
    fun `test loadRecipes initial recipes are loaded`() = runTest {
        viewModel.loadRecipes()
        val result = viewModel.recipes.first()
        assert(result == recipes)
    }

    /**
     * This function tests whether given the recipes, when the changeCategory function is called, it modifies the list
     */
    @Test
    fun `test category filter`() = runTest {
        viewModel.onCategorySelected("Snack")
        val filteredRecipes = viewModel.recipes.first()

        assertEquals(1, filteredRecipes.size)
        assertEquals("Vegetable Stir Fry", filteredRecipes[0].name)
    }

    /**
     * This function tests whether a change in the searchQuery affects the list fo recipes
     */
    @Test
    fun `test search query`() = runTest {
        viewModel.onSearchQueryChanged("Vegetable")
        val filteredRecipes = viewModel.recipes.first()

        assertEquals(1, filteredRecipes.size)
        assertEquals("Vegetable Stir Fry", filteredRecipes[0].name)
    }

    @Test
    fun `test vegetarian filter`() = runTest {
        viewModel.onVegetarianFilterSelected(true)
        val filteredRecipes = viewModel.recipes.first()

        assertEquals(1, filteredRecipes.size)
        assertEquals("Vegetable Stir Fry", filteredRecipes[0].name)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test save recipe`() = runTest {
        val recipe = Recipe(3, "New Recipe", "Dessert", true, listOf("Sugar", "Flour"), "Mix ingredients and bake.", true, "", "", "")
        viewModel.saveRecipe(recipe)
        advanceUntilIdle()
        coVerify(exactly = 1) { repository.saveRecipe(recipe) }
    }

    @Test
    fun `test get recipe by id id_exists`() = runTest {
        val recipe = viewModel.getRecipeById(2).first()

        assertNotNull(recipe)
        assertEquals("Vegetable Stir Fry", recipe?.name)
    }

    @Test
    fun `test get recipe by id id_does_not_exist`() = runTest {
        val recipe = viewModel.getRecipeById(10).first()

        assertEquals(null, recipe)
    }


}