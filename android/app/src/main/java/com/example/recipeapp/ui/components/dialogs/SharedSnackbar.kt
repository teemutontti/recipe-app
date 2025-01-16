package com.example.recipeapp.ui.components.dialogs

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.recipeapp.viewmodels.BaseViewModel

@Composable
fun SharedSnackbar(viewModel: BaseViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.alert) {
        viewModel.alert?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModel.clearAlert()
        }
    }
}