package com.example.feature_post

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.data.BASIC_CATEGORY
import com.farmer.data.DatabaseModule
import com.farmer.data.DateInfo
import com.farmer.data.History
import com.farmer.feature_post.R
import com.farmer.data.repository.OliveRepository
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun EditCash(
    dateInfo: DateInfo?,
    isSpend: Boolean,
    spendData: History.Transact.TransactData,
    onDismissRequest: () -> Unit,
    viewModel: PostViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val categoryList by selectCategoryList(viewModel).collectAsState(initial = emptyList())

    val uiState = viewModel.uiState.collectAsState()

    //드롭다운 메뉴 제어 변수
    var menuExpanded by remember {mutableStateOf(false)}

    var selectedOptionText by remember { mutableStateOf("")}
    var nameText by remember { mutableStateOf(TextFieldValue(spendData.item)) }
    var priceText by remember {mutableStateOf(TextFieldValue(spendData.price.toString()))}
    var categoryText by remember {mutableStateOf(spendData.category)}
    var yearText by remember { mutableStateOf(dateInfo?.date!!.year)}
    var monthText by remember { mutableStateOf(dateInfo?.date!!.monthNumber - 1)}
    var dayText by remember { mutableStateOf(dateInfo?.date!!.dayOfMonth) }
    var isSpendChip by remember { mutableStateOf(isSpend) }
    viewModel.yearState.value = yearText
    viewModel.monthState.value = monthText
    viewModel.dayOfMonthState.value = dayText
    viewModel.category.value = categoryText
    viewModel.name.value = nameText
    viewModel.amount.value = priceText

    viewModel.setChipState(isSpend = isSpendChip)


    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp)
    ) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.yearState.value = year
                viewModel.monthState.value = month
                viewModel.dayOfMonthState.value = dayOfMonth
                yearText = year
                monthText = month
                dayText = dayOfMonth
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH],
        )

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(
                modifier = Modifier.width(125.dp)
                    .clip(RoundedCornerShape(percent = 50)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor =
                    if (uiState.value.isSpendState ) Color(0x8092C88D)
                    else Color(0x80C2C2C2)
                ),
                onClick = { viewModel.setChipState(isSpend = true)
                    isSpendChip = true},
                elevation = null
            ) {
                Icon(
                    Icons.Outlined.Payments,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                    tint =
                    if (uiState.value.isSpendState) Color.DarkGray
                    else Color.Gray
                )
                Text(
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    text = "지출",
                    fontSize = 15.sp,
                    fontWeight =
                    if (uiState.value.isSpendState) FontWeight.Bold
                    else FontWeight.Normal,
                    color =
                    if (uiState.value.isSpendState) Color.DarkGray
                    else Color.Gray,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                modifier = Modifier
                    .width(125.dp)
                    .clip(RoundedCornerShape(percent = 50)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor =
                    if (!uiState.value.isSpendState) Color(0x8092C88D)
                    else Color(0x80C2C2C2)
                ),
                onClick = { viewModel.setChipState(isSpend = false)
                    isSpendChip = false},
                elevation = null
            ) {
                Icon(
                    Icons.Outlined.Savings,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                    tint =
                    if (!uiState.value.isSpendState) Color.DarkGray
                    else Color.Gray

                )
                Text(
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    text = "수입",
                    fontSize = 15.sp,
                    fontWeight =
                    if (!uiState.value.isSpendState) FontWeight.Bold
                    else FontWeight.Normal,
                    color =
                    if (!uiState.value.isSpendState) Color.DarkGray
                    else Color.Gray
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(end = 50.dp)
                    .align(Alignment.CenterVertically),
                text = "내역",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(
                modifier = Modifier.align(Alignment.Bottom),
                value = nameText,
                onValueChange = { nameText = it
                    viewModel.name.value = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedLabelColor = Color(0x8092C88D)
                ),
                readOnly = false,
                enabled = true
            )
        }
        AnimatedVisibility(visible = uiState.value.needNameState) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(all = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "내역명을 입력해주세요.",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(end = 50.dp)
                    .align(Alignment.CenterVertically),
                text = "금액",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Text(text = "원") },
                value = priceText,
                onValueChange = { priceText = it
                    viewModel.amount.value = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedLabelColor = Color(0x8092C88D)
                )
            )
        }
        AnimatedVisibility(visible = uiState.value.needAmountState) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(all = 5.dp), horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "금액을 입력해주세요.",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(end = 50.dp)
                    .align(Alignment.CenterVertically),
                text = "날짜",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            TextField(
                modifier = Modifier.fillMaxSize()
                    .align(Alignment.Bottom),
                trailingIcon = {
                    IconButton(onClick = {
                        timePickerDialog.show()
                    }) {
                        Icon(
                            Icons.Filled.EditCalendar,
                            contentDescription = null,
                            tint = Color(0xFF537150)
                        )
                    }
                },
                value = "${yearText} / ${monthText + 1} / ${dayText}",
                onValueChange = { },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    disabledTextColor = Color.DarkGray
                ),
                enabled = false,
            )
        }
        AnimatedVisibility(visible = uiState.value.needDateState) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "날짜를 입력해주세요.",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                modifier = Modifier.padding(end = 16.dp)
                    .align(Alignment.CenterVertically),
                text = "카테고리",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                expanded = menuExpanded,
                onExpandedChange = {
                    menuExpanded = !menuExpanded
                }
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = categoryText,
                    onValueChange = { categoryText = selectedOptionText
                        viewModel.category.value = selectedOptionText },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = menuExpanded
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        disabledTextColor = Color.DarkGray
                    )
                )
                ExposedDropdownMenu(
                    modifier = Modifier.height(200.dp),
                    expanded = menuExpanded,
                    onDismissRequest = {
                        menuExpanded = false
                    }
                ) {
                    categoryList?.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                viewModel.category.value = selectedOptionText
                                categoryText = selectedOptionText
                                menuExpanded = false
                            }
                        ) {
                            Text(text = selectionOption)
                        }
                    }

                }
            }

        }
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Button(onClick = {
                viewModel.viewModelScope.launch {
                    viewModel.deleteDataToEdit(
                        historyId = dateInfo?.history?.id,
                        transactionId = spendData.id
                    )
                    viewModel.postCashData()
                }
            },
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "수정하기")
            }
        }
        if (uiState.value.dismissDialogState) {
            viewModel.refreshState()
            onDismissRequest()
        }

        // loading
        AnimatedVisibility(visible = uiState.value.isLoading) {
            Dialog(
                onDismissRequest = { },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                ),
            ) {
                Surface(
                    modifier = Modifier.padding(12.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .progressSemantics()
                                .size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "로딩 중입니다.")
                    }
                }
            }
        }
    }
}

