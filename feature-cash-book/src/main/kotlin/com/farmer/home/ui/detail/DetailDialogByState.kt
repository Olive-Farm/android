package com.farmer.home.ui.detail

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.home.ui.states.CalendarUiState
import com.farmer.home.ui.states.CalendarViewModel
import com.farmer.home.util.DIALOG_HEIGHT

@Composable
fun DetailDialogByState(
    uiState: CalendarUiState,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    if (uiState is CalendarUiState.CalendarState && uiState.showDetailDialog) {
        Dialog(
            onDismissRequest = {
                viewModel.setDetailDialogState(shouldShow = false)
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Surface(
                modifier = Modifier.height(DIALOG_HEIGHT.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                DetailDialog(
                    dateInfo = uiState.clickedDateInfo,
                    isDialogEditMode = uiState.isDialogEditMode
                )
            }
        }
    }
}
