package com.example.recipeapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Dashboard

@Composable
fun NavBar(navController: NavController, selected: String) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxWidth().height(80.dp)
    ) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth().shadow(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavButton(
                    selected = selected == "discover",
                    onClick = { navController.navigate("discover") },
                    iconVector = Icons.Filled.Dashboard,
                    text = "Discover"
                )
                NavButton(
                    selected = selected == "cookbook",
                    onClick = { navController.navigate("cookbook") },
                    iconVector = Icons.AutoMirrored.Rounded.List,
                    text = "Cookbook"
                )
            }
        }
    }
}

/* OLD CODE
Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .background(
                    if (selected == "search") MaterialTheme.colorScheme.inversePrimary
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor =
                if (selected == "search") MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.inversePrimary
            ),
            onClick = { if (selected != "search") navController.navigate("search") }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = "Search"
                )
                Text(text = "Search")
            }
        }
        Button(
            modifier = Modifier
                .weight(1f)
                .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(1.dp))
                .background(
                    if (selected == "home") MaterialTheme.colorScheme.inversePrimary
                    else MaterialTheme.colorScheme.onBackground
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor =
                    if (selected == "home") MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.inversePrimary
            ),
            onClick = { if (selected != "home") navController.navigate("home") }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.Home,
                    contentDescription = "Home"
                )
                Text(text = "Home")
            }
        }
        Button(
            modifier = Modifier
                .weight(1f)
                .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(1.dp))
                .background(
                    if (selected == "my_recipes") MaterialTheme.colorScheme.inversePrimary
                    else MaterialTheme.colorScheme.onBackground
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor =
                if (selected == "my_recipes") MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.inversePrimary
            ),
            onClick = { if (selected != "my_recipes") navController.navigate("my_recipes") }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.List,
                    contentDescription = "List"
                )
                Text(text = "My Recipes")
            }
        }
    }
 */