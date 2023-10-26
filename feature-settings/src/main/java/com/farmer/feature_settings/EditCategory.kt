package com.farmer.feature_settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

//@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionApi::class)
@Composable
fun EditCategory(
    onDismissRequest: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
){
    val uiState = viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        Row(
            modifier = Modifier.padding(top = 3.dp, bottom = 3.dp, start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "카테고리 설정", fontWeight = FontWeight.Bold, fontSize = 23.sp)

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { },
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.DarkGray)
            }

        }
        Divider(modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp), color = Color(0xFF537150))
        if (uiState.value.dismissDialogState) {
            viewModel.refreshState()
            onDismissRequest()
        }
    }

}