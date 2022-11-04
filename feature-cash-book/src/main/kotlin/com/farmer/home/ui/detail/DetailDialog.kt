package com.farmer.home.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmer.home.ui.states.DateUiInfo
import com.farmer.home.util.toCommaString

@Composable
fun DetailDialog(
    dateInfo: DateUiInfo
) {
    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = "${dateInfo.dateOfMonth}", fontWeight = FontWeight.Bold, fontSize = 26.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = dateInfo.dayOfWeek.name.lowercase(), fontSize = 16.sp)
        }

        Divider(modifier = Modifier.padding(vertical = 6.dp), color = Color.DarkGray)

        LazyColumn {
            itemsIndexed(dateInfo.incomeList) { index, incomeData ->
                Row {
                    val titleVisibility = if (index == 0) 1f else 0f
                    Text(
                        text = "Income",
                        modifier = Modifier.alpha(titleVisibility),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = incomeData.toCommaString(), color = Color.Green)
                    Text(text = "￦")
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
            itemsIndexed(dateInfo.spendList) { index, spendData ->
                Row {
                    val titleVisibility = if (index == 0) 1f else 0f
                    Text(
                        text = "Spend",
                        modifier = Modifier.alpha(titleVisibility),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = spendData.toCommaString(), color = Color.Red)
                    Text(text = "￦")
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }


        Spacer(modifier = Modifier.height(12.dp))

        Divider(thickness = 0.5.dp, color = Color.Gray)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Sum of Income : ${dateInfo.sumOfIncome.toCommaString()} ￦",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Sum of spend : ${dateInfo.sumOfSpend.toCommaString()} ￦",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(modifier = Modifier.align(Alignment.End), imageVector = Icons.Default.Edit, contentDescription = null, tint = Color.Gray)
    }
}