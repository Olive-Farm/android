package com.farmer.home.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmer.home.ui.states.DateUiInfo

@Composable
fun DetailDialog(
    dateInfo: DateUiInfo
) {
    Column (modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = "${dateInfo.dateOfMonth}", fontWeight = FontWeight.Bold, fontSize = 26.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = dateInfo.dayOfWeek.name.lowercase(), fontSize = 16.sp)
        }
        Spacer(modifier = Modifier
            .width(12.dp)
            .fillMaxWidth())
        Text(text = "입금 : ${dateInfo.sumOfIncome}")
        Spacer(modifier = Modifier
            .width(6.dp)
            .fillMaxWidth())
        Text(text = "출금 : ${dateInfo.sumOfSpend}")
    }
}