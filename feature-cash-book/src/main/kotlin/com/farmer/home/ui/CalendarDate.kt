package com.farmer.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.DayOfWeek
import java.text.DecimalFormat

@Composable
fun CalendarDate(
    date: String,
    income: Int,
    spend: Int,
    dayOfWeek: kotlinx.datetime.DayOfWeek?,
    onClick: () -> Unit
) {
    val numFormat = DecimalFormat("#,###")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .size(90.dp)
            .padding(5.dp)
    ) {
        Text(text = date, color =
            if (dayOfWeek == java.time.DayOfWeek.SATURDAY) BlueAlpha200
            else if (dayOfWeek == java.time.DayOfWeek.SUNDAY) RedAlpha200
            else Color.DarkGray
        )
        Spacer(modifier = Modifier.weight(1f))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            backgroundColor = RedAlpha200,

        ) {
            if (income != 0) {
                Text(
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                    text = numFormat.format(income).toString(),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.size(2.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            backgroundColor = BlueAlpha200
        ) {
            if (spend != 0) {
                Text(
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                    text = numFormat.format(spend).toString(),
                    fontSize = 10.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun CalendarDatePreview() {
    CalendarDate(
        date = "28",
        income = 500000,
        spend = 90000,
        dayOfWeek = java.time.DayOfWeek.SATURDAY,
        onClick = {}
    )
}