package com.example.recipeapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.R
import com.example.recipeapp.ui.components.RecipeCard
import com.example.recipeapp.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorites(
    navController: NavController,
    viewModel: RecipeViewModel,
    isSearchActive: Boolean,
    onSearchActiveChange: (Boolean) -> Unit
) {
    val categories = listOf("All", "Dessert", "Main Course", "Snack")

    val selectedCategory = viewModel.selectedCategory.collectAsState().value
    val isVegetarianFilter = viewModel.isVegetarianFilter.collectAsState().value
    val recipes = viewModel.recipes.collectAsState(initial = emptyList()).value
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Image(
                    painter = painterResource(id = R.drawable.hungr),
                    contentDescription = "Hungr",
                    Modifier.size(100.dp)
                )
            }, actions = {
                IconButton(onClick = {
                    onSearchActiveChange(!isSearchActive)
                    if (!isSearchActive) {
                        viewModel.onSearchQueryChanged("")
                    }
                }) {
                    Icon(if (isSearchActive) Icons.Default.Close else Icons.Default.Search, contentDescription = "Search")
                }
            },
                modifier = Modifier.padding(10.dp)
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = "Favorite Recipes",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )
            // search box on top
            if (isSearchActive) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        viewModel.onSearchQueryChanged(it)
                    },
                    placeholder = { Text("Let him cook...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedIndicatorColor = Color.Transparent,  // Removes the bottom line when focused
                        unfocusedIndicatorColor = Color.Transparent,  // Removes the bottom line when unfocused
                        disabledIndicatorColor = Color.Transparent,  // Removes the bottom line when disabled
                        errorIndicatorColor = Color.Transparent  // Removes the bottom line when there's an error
                    ),
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search", Modifier.padding(start = 20.dp))
                    }
                )
            }

            Row(

            ){
                FilterChip(
                    selected = isVegetarianFilter == true,
                    onClick = {
                        if (isVegetarianFilter == false || isVegetarianFilter == null)
                            viewModel.onVegetarianFilterSelected(true)
                        else
                            viewModel.onVegetarianFilterSelected(null)
                    },
                    label = { Text("Veg") },
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFc3ffbd),
                    )
                )
                FilterChip(
                    selected = isVegetarianFilter == false,
                    onClick = {
                        if (isVegetarianFilter == true || isVegetarianFilter == null)
                            viewModel.onVegetarianFilterSelected(false)
                        else
                            viewModel.onVegetarianFilterSelected(null)
                    },
                    label = { Text("Non-Veg") },
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFff8c98),
                    )
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    items(categories) {category ->
                        ElevatedFilterChip(
                            selected = selectedCategory == category,
                            onClick = { viewModel.onCategorySelected(if (category == "All") null else category) },
                            label = { Text(category) }
                        )
                    }
                }
            }
            // list of recipes
            val savedRecipes = recipes.filter { it.isSaved }
            LazyVerticalGrid (
                columns = GridCells.Fixed(2),
                Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(savedRecipes) { recipe ->
                    if(recipe.isSaved){
                        RecipeCard(recipe = recipe, onClick = {
                            navController.navigate("recipeDetails/${recipe.id}")
                        }, viewModel)
                    }
                }
            }
        }
    }
}
