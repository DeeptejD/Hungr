package com.example.recipeapp.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.recipeapp.R
import com.example.recipeapp.ui.components.RecipeCard
import com.example.recipeapp.util.Greeting
import com.example.recipeapp.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: RecipeViewModel,
    isSearchActive: Boolean,
    onSearchActiveChange: (Boolean) -> Unit // This just toggles the isSearchActive parameter
    ) {
    val categories = listOf("All", "Breakfast", "Main Course",  "Dessert", "Snack")

    val selectedCategory = viewModel.selectedCategory.collectAsState().value
    val isVegetarianFilter = viewModel.isVegetarianFilter.collectAsState().value
    val searchQuery by viewModel.searchQuery.collectAsState()

    val recipes = viewModel.recipes.collectAsState(initial = emptyList()).value
    val context = LocalContext.current
    // instance of the greeting object to change the greeting dynamically
    val greeting = Greeting()

    // Remember speech recognition launcher
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        var spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)

        if (spokenText != null) {
            if (spokenText.equals("desert", ignoreCase = true)) {
                spokenText = "dessert"
            }
            when {
                spokenText.equals("non veg", ignoreCase = true) || spokenText.equals("nonveg", ignoreCase = true) -> {
                    viewModel.onSearchQueryChanged("")
                    viewModel.onVegetarianFilterSelected(false)
                }
                spokenText.equals("veg", ignoreCase = true) -> {
                    viewModel.onSearchQueryChanged("")
                    viewModel.onVegetarianFilterSelected(true)
                }
                spokenText.equals("favourite", ignoreCase = true) || spokenText.equals("favorites", ignoreCase = true) || spokenText.equals("favourites", ignoreCase = true) || spokenText.equals("favorite", ignoreCase = true) -> {
                    navController.navigate("favorites") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    viewModel.onCategorySelected(null)
                    viewModel.onSearchQueryChanged("")
                }

                categories.any { it.equals(spokenText, ignoreCase = true) } -> {
                    val matchingCategory = categories.find { it.equals(spokenText, ignoreCase = true) }
                    viewModel.onCategorySelected(if (matchingCategory == "All") null else matchingCategory)
                    // Reset vege and search query
                    if (matchingCategory == "All") {
                        Toast.makeText(context, "Showing all recipes", Toast.LENGTH_SHORT).show()
                        viewModel.onVegetarianFilterSelected(null)
                        viewModel.onSearchQueryChanged("")
                    }
                }

                spokenText.startsWith("search", ignoreCase = true) -> {
                    val dishName = spokenText.removePrefix("search").trim()
                    if (dishName.isNotEmpty()) {
                        if(dishName.equals("all", ignoreCase = true)) {
                            viewModel.onCategorySelected(null)
                            viewModel.onSearchQueryChanged("")
                            Toast.makeText(context, "Showing all recipes", Toast.LENGTH_SHORT).show()
                        }
                        else if (dishName.equals("veg", ignoreCase = true)) {
                            viewModel.onCategorySelected(null)
                            viewModel.onSearchQueryChanged("")
                            viewModel.onVegetarianFilterSelected(true)
                        } else if (dishName.equals("non veg", ignoreCase = true) || dishName.equals("nonveg", ignoreCase = true)) {
                            viewModel.onCategorySelected(null)
                            viewModel.onSearchQueryChanged("")
                            viewModel.onVegetarianFilterSelected(false)
                        }
                        else {
                            viewModel.onSearchQueryChanged(dishName)
                            Toast.makeText(context, "Showing recipes that match with, $dishName", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please specify a dish name", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    Toast.makeText(context, "Say \"Search\" followed by the name of the dish", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
//    val requestPermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//            startSpeechRecognition(speechRecognizerLauncher)
//        } else {
//            Toast.makeText(context, "Microphone permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }
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
                    // resets the search qry subsequently causing the entire recipe list to be reset
                    viewModel.onSearchQueryChanged("")
                }) {
                    // switches from a cancel to a search icon
                    Icon(if (isSearchActive) Icons.Default.Close else Icons.Default.Search, contentDescription = "Search")
                }
            }, modifier = Modifier.padding(10.dp))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                    when {
//                        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED -> {
//                            startSpeechRecognition(speechRecognizerLauncher)
//                        }
//                        else -> {
//                            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//                        }
//                    }
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Say \"Search\" followed by a dish name, or simply mention a category!")
                    }
                    speechRecognizerLauncher.launch(intent)
                },
                containerColor = Color(0xFFFF6A16),
                contentColor = Color.White
            ) {
                Icon(painter = painterResource(id = R.drawable.mic), contentDescription = "Speak")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Displays the greeting message
            val calendar = java.util.Calendar.getInstance()
            Text(
                text = greeting.getGreetingMessage(calendar),
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
                    placeholder = { Text("What are you craving for?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFFFCEB3),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search", Modifier.padding(start = 20.dp))
                    }
                )
            }

            // filter chips
            Row(
            ) {
                FilterChip(
                    selected = isVegetarianFilter == true,
                    onClick = {
                        if (isVegetarianFilter == false || isVegetarianFilter == null)
                            viewModel.onVegetarianFilterSelected(true)
                        else
                            viewModel.onVegetarianFilterSelected(null)
                    },
                    label = { Text("Veg") },
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom=10.dp),
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
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom=10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFff8c98),
                    )
                )
                LazyRow(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(categories) { category ->
                        ElevatedFilterChip(
                            selected = selectedCategory == category,
                            onClick = {
                                viewModel.onCategorySelected(if (category == "All") null else category) },
                            label = { Text(category) }
                        )
                    }
                }
            }

            // list of recipes
            LazyVerticalGrid (
                columns = GridCells.Fixed(2),
                Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(recipes) { recipe ->
                    RecipeCard(recipe = recipe, onClick = {
                        navController.navigate("recipeDetails/${recipe.id}")
                    }, viewModel)
                }
            }
        }
    }
}
