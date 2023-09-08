package com.example.feature_post

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.feature_post.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun AddCash(
    onDismissRequest: () -> Unit,
    viewModel: PostViewModel = hiltViewModel()
) {
    val context = LocalContext.current

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
            } else print("camera permission is denied")
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
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH],
        )

        Row(modifier = Modifier.padding(all = 7.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "새로운 내역 입력", fontWeight = FontWeight.Bold, fontSize = 4.5.em)

            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                cameraPermissionState.launchPermissionRequest()
            }) {
                Icon(
                    painterResource(id = R.drawable.baseline_camera_enhance_24),
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Icon(Icons.Default.Share, contentDescription = null)
            }
        }

        Divider(modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp), color = Color(0xFF355A1E))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("내역")
            },
            value = viewModel.name.value,
            onValueChange = { viewModel.name.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray
            )
        )
        AnimatedVisibility(visible = uiState.value.needNameState) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "가게명을 입력해주세요.",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("금액")
            },
            trailingIcon = {Text (text="원")},
            value = viewModel.amount.value,
            onValueChange = { viewModel.amount.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray
            )
        )
        AnimatedVisibility(visible = uiState.value.needAmountState) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "금액을 입력해주세요.",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row {
            TextButton(onClick = {
                timePickerDialog.show()
            }) {
                Text(text = "날짜 선택", color = Color(0xFF355A1E))
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text =
                if (viewModel.yearState.value ==0) ""
                else "${viewModel.yearState.value}년 ${viewModel.monthState.value +1}월 ${viewModel.dayOfMonthState.value}일 "
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Row (modifier = Modifier.align(Alignment.CenterHorizontally)){
            Chip(
                colors = ChipDefaults.chipColors(
                    backgroundColor =
                    if (uiState.value.isSpendState) Color(0xFF355A1E)
                    else Color(0xFFE8F5E9)
                ),
                onClick = { viewModel.setChipState(isSpend = true) }
            ) {
                Text(text = "소비", color =
                if (uiState.value.isSpendState) Color.White
                else Color.DarkGray)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Chip(
                colors = ChipDefaults.chipColors(
                    backgroundColor =
                    if (!uiState.value.isSpendState) Color(0xFF355A1E)
                    else Color(0xFFE8F5E9)
                ),
                onClick = { viewModel.setChipState(isSpend = false) }
            ) {
                Text(text = "수입", color =
                if (!uiState.value.isSpendState) Color.White
                else Color.DarkGray)
            }

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
        Spacer(modifier = Modifier.weight(1f))
        Row {
            Button(onClick = viewModel::postCashData,
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "등록하기")
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = viewModel::postCashData) {
                Icon(Icons.Filled.Check, contentDescription = null)
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