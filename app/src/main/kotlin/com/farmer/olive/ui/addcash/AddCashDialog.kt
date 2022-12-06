package com.farmer.olive.ui.addcash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddCashDialog() {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp)
    ) {
        var tempText by remember { mutableStateOf(TextFieldValue("")) }
        TextField(value = tempText, onValueChange = { tempText = it })
    }
}

@Preview
@Composable
fun AddCashDialogPreview() {
    AddCashDialog()
}