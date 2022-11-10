package com.farmer.feature_statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatisticsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚠️ Under Construction ⚠️",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

//        Image(
//            modifier = Modifier.size(140.dp),
//            painter = painterResource(R.drawable.img_working_on),
//            contentDescription = null
//        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Team Olive is currently working on it.",
            fontSize = 14.sp
        )
    }
}