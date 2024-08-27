package com.example.recipeapp
//
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import com.example.recipeapp.data.model.Recipe
//import com.example.recipeapp.ui.components.RecipeCard
//import com.example.recipeapp.viewmodel.RecipeViewModel
//import io.mockk.impl.annotations.MockK
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
class RecipeCardTest {
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @MockK
//    lateinit var viewModel: RecipeViewModel
//
//    @Before
//    fun setup() {
////        viewModel = mockk(RecipeViewModel::class.java.toString())
//    }
//
//    @Test
//    fun testRecipeCardDisplaysRecipeDetails() {
//        val recipe = Recipe(
//            id=1,
//            name = "Spaghetti Bolognese",
//            category = "Italian",
//            cookingTime = "30 minutes",
//            imageResId = "spaghetti_image",
//            isVegetarian = false,
//            isSaved = false,
//            description = "",
//            ingredients = listOf(),
//            instructions = ""
//        )
//
//        composeTestRule.setContent {
//            RecipeCard(recipe = recipe, onClick = {}, viewModel = viewModel)
//        }
//
//        composeTestRule.onNodeWithText("Spaghetti Bolognese").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Italian").assertIsDisplayed()
//        composeTestRule.onNodeWithText("30 minutes").assertIsDisplayed()
//    }
}