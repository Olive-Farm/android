package com.example.feature_post

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.feature_post.model.UserPostInput
import java.util.*

@Composable
fun AddCash(
    onDismissRequest: () -> Unit,
    viewModel: PostViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp)
    ) {
        var nameText by remember { mutableStateOf(TextFieldValue("")) }
        var amountText by remember { mutableStateOf(TextFieldValue("")) }
        val calendar = Calendar.getInstance()
        val yearState = remember { mutableStateOf(0) }
        val monthState = remember { mutableStateOf(0) }
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
            label = {
                Text("Name")
            },
            value = nameText,
            onValueChange = { nameText = it }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            label = {
                Text("Amount")
            },
            value = amountText,
            onValueChange = { amountText = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = {
            timePickerDialog.show()
        }) {
            Text("날짜 선택")
        }
        Text(text = "${yearState.value}/${monthState.value}/${dayOfMonthState.value}")
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
                onDismissRequest()
            }) {
                Icon(Icons.Filled.Check, contentDescription = null)
            }
        }
    }
}
