package com.example.recipeapp.composables

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipeapp.ApplicationContext
import com.example.recipeapp.repositories.RecipeRepository

@Composable
fun DeleteDialog(navController: NavController, exitDialog: () -> Unit) {
    val context = ApplicationContext.current
    val recipeViewModel: RecipeRepository = viewModel(LocalContext.current as ComponentActivity)

    fun handleDelete() {
        exitDialog()
        recipeViewModel.deleteOwnRecipe(context)
        navController.navigate("cookbook")
    }

    Dialog(onDismissRequest = { exitDialog() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            Text(
                text = "Are you sure you want to delete this recipe?",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                )
            Spacer(modifier = Modifier.height(8.dp))
            Text("This action cannot be undone!")
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { exitDialog() }) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { handleDelete() }) {
                    Text("Delete")
                }
            }
        }
    }
}