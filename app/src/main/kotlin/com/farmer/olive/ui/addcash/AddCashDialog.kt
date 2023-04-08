package com.farmer.olive.ui.addcash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.home.ui.TempCalendarViewModel
import com.farmer.home.ui.states.CalendarViewModel

@Composable
fun AddCashDialog(
    viewModel: CalendarViewModel = hiltViewModel(),
    tempViewModel: TempCalendarViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp)
    ) {
        var timeText by remember { mutableStateOf(TextFieldValue("")) }
        var nameText by remember { mutableStateOf(TextFieldValue("")) }
        var amountText by remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            label = {
                Text("Time")
            },
            value = timeText,
            onValueChange = { timeText = it }
        )
        Spacer(modifier = Modifier.height(12.dp))
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
        Spacer(modifier = Modifier.weight(1f))
        Row {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                // todo tempviewmodel로 옮기기
                viewModel.sendInputCashDataAndDismiss(
                    timeText.text,
                    nameText.text,
                    amountText.text
                )
            }) {
                Icon(Icons.Filled.Check, contentDescription = null)
            }
        }
    }
}
