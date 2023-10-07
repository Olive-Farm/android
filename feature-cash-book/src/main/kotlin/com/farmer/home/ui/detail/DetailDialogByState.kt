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
import com.farmer.home.ui.DialogUiState.DetailDialog
import com.farmer.home.ui.TempCalendarViewModel
import com.farmer.home.util.DIALOG_HEIGHT

@Composable
fun DetailDialogByState(
    dialogUiState: DetailDialog,
    viewModel: TempCalendarViewModel = hiltViewModel()
) {
    Dialog(
        onDismissRequest = {
            viewModel.setShowDetailDialog(shouldShow = false, null)
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
                dateInfo = dialogUiState.clickedDateInfo,
                isDialogEditMode = false,
            )
        }
    }
}
