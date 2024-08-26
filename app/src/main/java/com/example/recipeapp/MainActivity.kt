package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.repository.RecipeRepositoryImpl
import com.example.recipeapp.ui.screens.Favorites
import com.example.recipeapp.ui.screens.HomeScreen
import com.example.recipeapp.ui.screens.RecipeDetailsScreen
import com.example.recipeapp.ui.theme.RecipeAppTheme
import com.example.recipeapp.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val repository = RecipeRepositoryImpl(applicationContext)
        val viewModel = RecipeViewModel(repository)
        setContent {
            RecipeAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {
                    RecipeApp(viewModel)
                }
            }
        }
    }
}

// This is the data class that defines the navigation items
data class NavigationItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun RecipeApp(viewModel: RecipeViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    // This retusns the sring val of the current path
    val currentRoute = currentBackStackEntry?.destination?.route

    // These are displayed on the bottom nav
    val items = listOf(
        NavigationItem("Home", Icons.Filled.Home, "home"),
        NavigationItem("Favorites", Icons.Filled.Favorite, "favorites")
    )

    // This is used to control the search bar visibility and maintain it across screens
    var isSearchActive by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", Modifier.padding(innerPadding)) {
            composable("home") { HomeScreen(navController, viewModel, isSearchActive, onSearchActiveChange = { isSearchActive = it }) }
            composable("favorites") { Favorites(navController, viewModel, isSearchActive, onSearchActiveChange = { isSearchActive = it }) }
            composable("recipeDetails/{recipeId}") { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
                val recipe = recipeId?.let { viewModel.getRecipeById(it).collectAsState(initial = null).value }
                if (recipe != null) {
                    RecipeDetailsScreen(navController, recipe)
                }
            }
        }
    }
}
