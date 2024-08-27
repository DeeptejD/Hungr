package com.example.recipeapp

//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.assertIsNotDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.navigation.NavController
//import com.example.recipeapp.ui.screens.HomeScreen
//import com.example.recipeapp.viewmodel.RecipeViewModel
//import org.junit.Rule
//import org.junit.Test
//
//class HomeScreenTest {
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    val viewModel: RecipeViewModel = mockk()
//
//    @Test
//    fun testVegetarianFilterDisplaysVegetarianRecipes() {
//        // Set up your test environment
//        val viewModel = RecipeViewModel() // You might need to mock or use a real instance
//
//        // Launch the HomeScreen with a test viewModel
//        composeTestRule.setContent {
//            HomeScreen(
//                navController = NavController(), // Use a fake or real navController
//                viewModel = viewModel,
//                isSearchActive = false,
//                onSearchActiveChange = {}
//            )
//        }
//
//        // Click the vegetarian filter
//        composeTestRule.onNodeWithText("Veg")
//            .performClick()
//
//        // Assert that only vegetarian recipes are displayed
//        val vegetarianRecipeTitles = listOf("Veg Recipe 1", "Veg Recipe 2") // Replace with actual titles of vegetarian recipes you expect
//        vegetarianRecipeTitles.forEach { title ->
//            composeTestRule.onNodeWithText(title)
//                .assertIsDisplayed()
//        }
//
//        // Assert that non-vegetarian recipes are not displayed
//        val nonVegetarianRecipeTitles = listOf("Non-Veg Recipe 1", "Non-Veg Recipe 2") // Replace with actual titles of non-vegetarian recipes
//        nonVegetarianRecipeTitles.forEach { title ->
//            composeTestRule.onNodeWithText(title)
//                .assertIsNotDisplayed()
//        }
//    }
//}