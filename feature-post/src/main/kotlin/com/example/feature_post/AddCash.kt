package com.example.feature_post

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.feature_post.model.UserPostInput
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddCash(
    onDismissRequest: () -> Unit,
    viewModel: PostViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp)
    ) {
        var nameText by remember { mutableStateOf(TextFieldValue("")) }
        var amountText by remember { mutableStateOf(TextFieldValue("")) }
        val calendar = Calendar.getInstance()
        val yearState = remember { mutableStateOf(0) }
        val monthState = remember { mutableStateOf(-1) }
        val dayOfMonthState = remember { mutableStateOf(0) }
        val context = LocalContext.current
        val timePickerDialog = DatePickerDialog(
            context,
            { view, year, month, dayOfMonth ->
                yearState.value = year
                monthState.value = month
                dayOfMonthState.value = dayOfMonth
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
            value = nameText,
            onValueChange = { nameText = it },
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
            value = amountText,
            onValueChange = { amountText = it },
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
                text = "${yearState.value}/${monthState.value + 1}/${dayOfMonthState.value}"
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
                viewModel.postCashData(
                    UserPostInput(
                        year = yearState.value,
                        month = monthState.value + 1,
                        date = dayOfMonthState.value,
                        name = nameText.text,
                        amount = amountText.text
                    )
                )
            }) {
                Icon(Icons.Filled.Check, contentDescription = null)
            }
        }
        if (uiState.value.dismissDialogState) {
            onDismissRequest()
        }
    }
}
