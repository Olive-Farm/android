package com.farmer.home.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.home.ui.states.CalendarUiState
import com.farmer.home.ui.states.CalendarViewModel

@Composable
fun DetailDialogByState(
    uiState: CalendarUiState,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    if (uiState is CalendarUiState.CalendarState && uiState.showDetailDialog) {
        Dialog(
            onDismissRequest = {
                viewModel.setDetailDialogState(shouldShow = false)
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 140.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                DetailDialog(
                    dateInfo = uiState.clickedDateInfo
                )
            }
        }
    }
}