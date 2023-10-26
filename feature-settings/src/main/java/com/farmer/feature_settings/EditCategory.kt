package com.farmer.feature_settings

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

//@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionApi::class)
@Composable
fun EditCategory(
    onDismissRequest: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
){
    val uiState = viewModel.uiState.collectAsState()

    Text("제발 떠라")
    if (uiState.value.dismissDialogState) {
        viewModel.refreshState()
        onDismissRequest()
    }
}