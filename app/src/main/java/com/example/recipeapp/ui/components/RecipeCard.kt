package com.example.recipeapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.data.model.Recipe
import com.example.recipeapp.viewmodel.RecipeViewModel

// The card that displays recipes
@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit, viewModel: RecipeViewModel) {
    // This fetches the painter res of the image id given the name of the image
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(recipe.imageResId, "drawable", context.packageName)
    val imagePainter = painterResource(id = imageResId)

    // remember is used to store a val across compositions, and mutableStateOf triggers a recomposition when the value changes
    var isSaved by remember { mutableStateOf(recipe.isSaved) }

    // main card
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth(),
        onClick = onClick, // clickable card, when clicked takes you to the details page
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = imagePainter,
                contentDescription = recipe.name,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentScale = ContentScale.Crop // This creates a cover image type appreance
            )

            //black gradient for the bg
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f)
                            ),
                            startY = 0f,
                            endY = 800f
                        )
                    )
            )

            // Veg/Non-Veg Icon
            Image(
                painter = painterResource(if (recipe.isVegetarian) R.drawable.vegicon else R.drawable.nonvegicon),
                contentDescription = if (recipe.isVegetarian) "Vegetarian" else "Non-Vegetarian",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .size(24.dp)
                    .background(Color.Transparent)
            )

            // we added a box since we wanted to add the clickable property, which isnt avail on an Icon
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clickable {
                        // when the fav icon is clicked, it changes isSaved, thus causing a recomposition, while preserving the save value
                        isSaved = !isSaved
                        // then it calls the saveRecipe function from the viewmodel which toggles the save state
                        viewModel.saveRecipe(recipe)
                    }
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (recipe.isSaved) "Unfavorite" else "Favorite",
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.Transparent),
                    tint = if (isSaved) Color.Red else Color.Black
                )
            }

            // A column that is aligned at the bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .background(Color.Transparent)
            ) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = recipe.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White, // Adjust color as needed for contrast
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = recipe.cookingTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}
