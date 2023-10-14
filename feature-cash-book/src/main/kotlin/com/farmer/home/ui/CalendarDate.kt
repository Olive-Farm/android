package com.farmer.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
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

    Column(         //일자 한 칸
        modifier = Modifier
            .fillMaxWidth()

            .clickable {
                onClick()
            }
            .padding(1.dp)
            .background(color =
            if (dayOfWeek == java.time.DayOfWeek.SATURDAY || dayOfWeek == java.time.DayOfWeek.SUNDAY) Color(0xFFF6F6F6)
            else Color.White,
                shape = RoundedCornerShape(size = 10.dp)
            )

            .border(width = 0.5.dp, color = Color(0x80C2C2C2), shape = RoundedCornerShape(size = 10.dp))

            .size(90.dp)
            .padding(5.dp)
    ) {
        Text(text = date, color =
            if (dayOfWeek == java.time.DayOfWeek.SATURDAY) BlueAlpha200
            else if (dayOfWeek == java.time.DayOfWeek.SUNDAY) RedAlpha200
            else Color.DarkGray,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(3.dp),
            backgroundColor = Color(0xFF52ACFF)
        ) {
            if (spend != 0) {
                Text(
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                    text = numFormat.format(spend).toString(),
                    fontSize = 10.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.size(2.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(3.dp),
            backgroundColor = Color(0xFFFF95CE),

            ) {
            if (income != 0) {
                Text(
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                    text = numFormat.format(income).toString(),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
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