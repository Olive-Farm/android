package com.example.feature_post

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.text.BoringLayout
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.data.BASIC_CATEGORY
import com.farmer.data.DatabaseModule
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
import java.time.LocalDate
import java.util.*


@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun AddCash(
    onDismissRequest: () -> Unit,
    viewModel: PostViewModel = hiltViewModel()
) {
    val context = LocalContext.current


    val categoryList by selectCategoryList(viewModel).collectAsState(initial = emptyList())


    var currentPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
    var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                currentPhotoUri = tempPhotoUri
                if (currentPhotoUri != null) {
                    val inputStream = context.contentResolver.openInputStream(currentPhotoUri)
                    viewModel.requestOcr(inputStream)
                }
            }
        }
    )
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { granted ->
            if (granted) {
                tempPhotoUri = context.createTempPictureUri()
                cameraLauncher.launch(tempPhotoUri)
            } else print("카메라 접근 권한을 허용해주세요.")
        }
    )

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val inputStream = context.contentResolver.openInputStream(uri)
                viewModel.requestOcr(inputStream)
            }
        }
    val uiState = viewModel.uiState.collectAsState()

    //카테고리 드롭다운 메뉴 제어 변수
    var menuExpanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }

    //영수증 추가 드롭다운 메뉴 제어 변수
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    //달력 초기화
    val todayDate = LocalDate.now().toString().split("-")
    var yearState by remember { mutableStateOf(todayDate[0].toInt()) }
    var monthState by remember { mutableStateOf(todayDate[1].toInt() - 1) }
    var dayState by remember { mutableStateOf(todayDate[2].toInt()) }
    viewModel.yearState.value = yearState
    viewModel.monthState.value = monthState
    viewModel.dayOfMonthState.value = dayState


    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp)
    ) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                yearState = year
                monthState = month
                dayState = dayOfMonth
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH],
        )

        Row(
            modifier = Modifier.padding(top = 3.dp, bottom = 3.dp, start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "새로운 내역 입력", fontWeight = FontWeight.Bold, fontSize = 23.sp)

            Spacer(modifier = Modifier.weight(1f))

            Box {
                IconButton(
                    onClick = { isDropDownMenuExpanded = true },
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = null, tint = Color.DarkGray)
                }
                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = isDropDownMenuExpanded,
                    onDismissRequest = { isDropDownMenuExpanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        cameraPermissionState.launchPermissionRequest()
                    }) {
                        Row {
                            Icon(
                                Icons.Filled.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Text("영수증 촬영하기")
                        }
                    }
                    DropdownMenuItem(onClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) {
                        Row {
                            Icon(
                                Icons.Filled.PhotoLibrary,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Text("영수증 불러오기")
                        }
                    }
                }
            }
        }

        Divider(modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp), color = Color(0xFF537150))
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 10f, fill = false)
        )
        {

            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(
                    modifier = Modifier.width(135.dp)
                        .clip(RoundedCornerShape(percent = 50)),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor =
                        if (uiState.value.isSpendState) Color(0x8092C88D)
                        else Color(0x80C2C2C2)
                    ),
                    onClick = { viewModel.setChipState(isSpend = true) },
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
                        .width(135.dp)
                        .clip(RoundedCornerShape(percent = 50)),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor =
                        if (!uiState.value.isSpendState) Color(0x8092C88D)
                        else Color(0x80C2C2C2)
                    ),
                    onClick = { viewModel.setChipState(isSpend = false) },
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
                    .padding(horizontal = 15.dp)
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
                    modifier = Modifier,
                    value = viewModel.name.value,
                    onValueChange = { viewModel.name.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedLabelColor = Color(0x8092C88D)
                    )
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
                    .padding(horizontal = 15.dp)
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
                    value = viewModel.amount.value,
                    onValueChange = { viewModel.amount.value = it },
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
                    .padding(horizontal = 15.dp)
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
                    value = "${viewModel.yearState.value} / ${viewModel.monthState.value + 1} / ${viewModel.dayOfMonthState.value}",
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
                    .padding(horizontal = 15.dp)
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
                        value = selectedOptionText,
                        onValueChange = { viewModel.category.value = selectedOptionText },
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
                                    menuExpanded = false
                                }
                            ) {
                                Text(text = selectionOption)
                            }
                        }

                    }
                }

            }

        }

        Spacer(modifier = Modifier.weight(1f))
        Row {
            Button(
                onClick = viewModel::postCashData,
                /*{
                    viewModel.postCashData()
                    viewModel.amount.value = TextFieldValue("")
                    viewModel.name.value = TextFieldValue("")
                }*/
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "등록하기")
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


private fun Context.createTempPictureUri(
    provider: String = "com.farmer.olive.provider",
//    fileName: String = "picture_${System.currentTimeMillis()}",
    fileName: String = "cache_picture",
    fileExtension: String = ".png"
): Uri {
    val tempFile = File.createTempFile(
        fileName, fileExtension, cacheDir
    ).apply {
        createNewFile()
    }

    return FileProvider.getUriForFile(applicationContext, provider, tempFile)
}

fun selectCategoryList(viewModel: PostViewModel): Flow<List<String>?> = flow {

    val categoryTextList = withContext(Dispatchers.IO) {
        viewModel.selectCategoryList()
    }
    emit(categoryTextList)
}