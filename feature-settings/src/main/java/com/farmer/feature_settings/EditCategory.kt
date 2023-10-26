package com.farmer.feature_settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

//@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionApi::class)
@Composable
fun EditCategory(
    onDismissRequest: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
){
    val uiState = viewModel.uiState.collectAsState()

    val categoryList by selectCategoryList(viewModel).collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        Row(
            modifier = Modifier.padding(top = 3.dp, bottom = 3.dp, start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "카테고리 설정", fontWeight = FontWeight.Bold, fontSize = 23.sp)

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { viewModel.setAddState() },
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.DarkGray)
            }

        }
        Divider(modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp), color = Color(0xFF537150))
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        )
        {
            AnimatedVisibility(visible = uiState.value.addState) {//
                Row(
                    modifier = Modifier.padding(all = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedTextField(
                        modifier = Modifier.height(50.dp)
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        value = "",
                        onValueChange = { },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedLabelColor = Color(0x8092C88D)
                        )
                    )
                    IconButton( modifier = Modifier.padding(all=5.dp)
                        .size(30.dp)
                        .align(Alignment.CenterVertically),
                        onClick = {  },
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = Color.DarkGray)
                    }
                }
            }
            categoryList?.forEach { category ->
                Row(modifier = Modifier.fillMaxWidth()
                ){
                    Box(
                        modifier = Modifier.padding(all=5.dp)
                            .fillMaxWidth()
                            .background(color = Color(0xFFF4F4F4),
                            shape = RoundedCornerShape(size = 12.dp))
                            .padding(all = 15.dp)
                    ) {
                        Text(modifier = Modifier.align(Alignment.CenterStart),
                            text = category,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray)

                        IconButton( modifier = Modifier.padding(all=5.dp)
                            .padding(end = 35.dp)
                            .size(20.dp)
                            .align(Alignment.CenterEnd),
                            onClick = {  },
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = null, tint = Color.Gray)
                        }
                        IconButton( modifier = Modifier.padding(all=5.dp)
                            .size(20.dp)
                            .align(Alignment.CenterEnd),
                            onClick = {  },
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = null, tint = Color.Gray)
                        }
                    }
                }
            }
        }

        if (uiState.value.dismissDialogState) {
            viewModel.refreshState()
            onDismissRequest()
        }
    }

}

fun selectCategoryList(viewModel: CategoryViewModel): Flow<List<String>?> = flow {

    val categoryTextList = withContext(Dispatchers.IO) {
        viewModel.selectCategoryList()
    }
    emit(categoryTextList)
}