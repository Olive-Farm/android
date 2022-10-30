package com.farmer.home.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailDialog() {
    Column (modifier = Modifier.padding(horizontal = 4.dp, 6.dp)) {
        Text("Hello World, This is detail dialog")
    }
}