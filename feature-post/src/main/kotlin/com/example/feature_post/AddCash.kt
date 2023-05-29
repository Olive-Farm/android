package com.example.feature_post

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Name")
            },
            value = viewModel.name.value,
            onValueChange = { viewModel.name.value = it },
        )
        AnimatedVisibility(visible = uiState.value.needNameState) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "적요를 입력해주세요.",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Amount")
            },
            value = viewModel.amount.value,
            onValueChange = { viewModel.amount.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
            Chip(
                colors = ChipDefaults.chipColors(
                    backgroundColor =
                    if (uiState.value.isSpendState) Color(0xFF4CAF50)
                    else Color(0xFFE8F5E9)
                ),
                onClick = { viewModel.setChipState(isSpend = true) }
            ) {
                Text("소비")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Chip(
                colors = ChipDefaults.chipColors(
                    backgroundColor =
                    if (!uiState.value.isSpendState) Color(0xFF4CAF50)
                    else Color(0xFFE8F5E9)
                ),
                onClick = { viewModel.setChipState(isSpend = false) }
            ) {
                Text("수입")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            TextButton(onClick = {
                timePickerDialog.show()
            }) {
                Text("날짜 선택")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "${viewModel.yearState.value}/${viewModel.monthState.value + 1}/${viewModel.dayOfMonthState.value}"
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
        Spacer(modifier = Modifier.weight(1f))
        Row {
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