package com.farmer.home.ui.detail

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.data.DateInfo
import com.farmer.home.ui.BlueAlpha200
import com.farmer.home.ui.RedAlpha200
import com.farmer.home.ui.states.CalendarViewModel

@Composable
fun DetailDialog(
    dateInfo: DateInfo?,
    isDialogEditMode: Boolean,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    Log.e("@@@dateInfo", ": $dateInfo")
    Log.e("@@@spendList", "spendList: ${dateInfo?.history?.spendList?.spendList}")
    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "${dateInfo?.date?.dayOfMonth}",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = dateInfo?.date?.dayOfWeek.toString(), fontSize = 16.sp)
        }

        Divider(modifier = Modifier.padding(vertical = 6.dp), color = Color.DarkGray)

        LazyColumn {
            itemsIndexed(
                dateInfo?.history?.spendList?.earnList ?: emptyList()
            ) { index, incomeData ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val titleVisibility = if (index == 0) 1f else 0f
                    Text(
                        text = "Income",
                        modifier = Modifier.alpha(titleVisibility),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // todo tocommastring
                    Text(text = incomeData.item)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = String.format("%,d", incomeData.price.toString().toLong()),
                        color = BlueAlpha200
                    )
                    Text(text = "￦")
                    if (isDialogEditMode) {
                        IconButton(
                            modifier = Modifier
                                .padding(0.dp)
                                .size(16.dp),
                            onClick = { /* todo */ }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Gray,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
            itemsIndexed(
                dateInfo?.history?.spendList?.spendList ?: emptyList()
            ) { index, spendData ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val titleVisibility = if (index == 0) 1f else 0f
                    Text(
                        text = "Spend",
                        modifier = Modifier.alpha(titleVisibility),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = spendData.item)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = String.format("%,d", spendData.price.toString().toLong()),
                        color = RedAlpha200
                    )
                    Text(text = "￦")
                    if (isDialogEditMode) {
                        IconButton(
                            modifier = Modifier
                                .padding(0.dp)
                                .size(16.dp),
                            onClick = { /* todo */ }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Gray,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }


        Spacer(modifier = Modifier.height(12.dp))

        Divider(thickness = 0.5.dp, color = Color.Gray)

        Spacer(modifier = Modifier.height(12.dp))

        val sumOfIncome = (dateInfo?.history?.spendList?.earnList ?: emptyList()).sumOf { it.price }
        Text(
            text = "Sum of Income : ${String.format("%,d", sumOfIncome.toString().toLong())} ￦",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        val sumOfSpend = (dateInfo?.history?.spendList?.spendList ?: emptyList()).sumOf { it.price }
        Text(
            text = "Sum of spend : ${String.format("%,d", sumOfSpend.toString().toLong())} ￦",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        Row {
            var inputText by remember { mutableStateOf(TextFieldValue("")) }
            if (isDialogEditMode) {
                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    value = inputText,
                    onValueChange = { inputText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            IconButton(onClick = { viewModel.changeEditModeState() }) {
                if (isDialogEditMode) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}