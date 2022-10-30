package com.farmer.home.ui.detail

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farmer.home.ui.states.DateUiInfo

@Composable
fun DetailDialog(
    dateInfo: DateUiInfo
) {
    Column (modifier = Modifier.padding(horizontal = 4.dp, 6.dp)) {
        Text(text = "${dateInfo.dateOfMonth}")
        Spacer(modifier = Modifier.width(12.dp).fillMaxWidth())
        Text(text = "입금 : ${dateInfo.sumOfIncome}")
        Spacer(modifier = Modifier.width(6.dp).fillMaxWidth())
        Text(text = "출금 : ${dateInfo.sumOfSpend}")
    }
}