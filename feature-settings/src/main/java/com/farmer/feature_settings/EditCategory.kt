package com.farmer.feature_settings

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

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
                        value = viewModel.newCategoryName.value,
                        onValueChange = { viewModel.newCategoryName.value = it },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedLabelColor = Color(0x8092C88D)
                        )
                    )
                    IconButton( modifier = Modifier.padding(all=5.dp)
                        .size(30.dp)
                        .align(Alignment.CenterVertically),
                        onClick = viewModel::addCategory,
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = Color.DarkGray)
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize()
            ){
               categoryList?.forEach{category ->
                   CategoryList(category)
               }
            }
        }

        if (uiState.value.dismissDialogState) {
            viewModel.refreshState()
            onDismissRequest()
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CategoryList(
    categoryName: String
    ){

    val swipeableState = rememberSwipeableState(initialValue = 0)
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.padding(all=5.dp)
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = mapOf(
                    0f to 0,
                    200f to 1,
                    200f to 2
                ),
                thresholds = { _, _ ->
                    FractionalThreshold(0.5f)
                },
                orientation = Orientation.Horizontal
            )
            .background(Color.White)
    ) {
        IconButton( modifier = Modifier.padding(all=5.dp)
            .size(20.dp)
            .align(Alignment.CenterStart),
            onClick = {  },
        ) {
            Icon(Icons.Filled.Edit, contentDescription = null, tint = Color.Gray)
        }
        IconButton( modifier = Modifier.padding(all=5.dp)
            .padding(start = 35.dp)
            .size(20.dp)
            .align(Alignment.CenterStart),
            onClick = {  },
        ) {
            Icon(Icons.Filled.Delete, contentDescription = null, tint = Color.Gray)
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .background(color = Color(0xFFF4F4F4),
                    shape = RoundedCornerShape(size = 12.dp))
                .fillMaxWidth()
                .padding(15.dp)
        ){

                Text(modifier = Modifier,
                    text = categoryName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray)

        }
    }
}

fun selectCategoryList(viewModel: CategoryViewModel): Flow<List<String>?> = flow {

    val categoryTextList = withContext(Dispatchers.IO) {
        viewModel.selectCategoryList()
    }
    emit(categoryTextList)
}