package com.farmer.home.ui.detail

import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.feature_post.PostViewModel
import com.farmer.data.DateInfo
import com.farmer.data.History
import com.farmer.data.HistoryList
import com.farmer.feature_post.R
import com.farmer.home.ui.BlueAlpha200
import com.farmer.home.ui.RedAlpha200
import com.farmer.home.ui.postdialog.PostDialogViewModel
import com.farmer.home.ui.states.CalendarViewModel
// 여기서 ui 수정?

@Composable
fun DetailDialog(
    dateInfo: DateInfo?,
    isDialogEditMode: Boolean,
    viewModel: CalendarViewModel = hiltViewModel(),
    postViewModel : PostDialogViewModel = hiltViewModel()
) {

    Log.e("@@@dateInfo", ": $dateInfo")
    Log.e("@@@spendList", "spendList: ${dateInfo?.history?.spendList?.spendList}")
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "${dateInfo?.date?.dayOfMonth}",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
            Spacer(modifier = Modifier.width(13.dp))
            Text(text = dateInfo?.date?.dayOfWeek.toString(), fontSize = 16.sp)
        }
        Divider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFF355A1E))

        if (dateInfo?.history?.spendList?.earnList.isNullOrEmpty() && dateInfo?.history?.spendList?.spendList.isNullOrEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "입력한 내역이 없습니다!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = Color(0xFF355A1E))) {
                        append("OliveBook")
                    }
                    withStyle(style = SpanStyle(color = Color.DarkGray)) {
                        append("을 이용하여\n 편리하게 가계부를 관리해보세요")
                    }
                }, fontSize = 14.sp,
                    textAlign = TextAlign.Center)
            }
        }
        else {
            LazyColumn {
                itemsIndexed(
                    dateInfo?.history?.spendList?.earnList ?: emptyList()
                ) { index, incomeData ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val titleVisibility = if (index == 0) 1f else 0f
                        Text(
                            text = "수입",
                            modifier = Modifier.alpha(titleVisibility),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        // todo tocommastring
                        Text(text = incomeData.item)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = String.format("%,d", incomeData.price.toString().toLong()),
                            color = BlueAlpha200
                        )
                        Text(text = " ￦")
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
                            text = "지출",
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

                        Text(text = "￦") // 나중에 이거 삭제하기 지금 테스트

                        /*if (isDialogEditMode) {
                            IconButton(
                                modifier = Modifier
                                    .padding(0.dp)
                                    .size(16.dp),
                                onClick = { *//* todo *//* }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Gray,
                            )
                        }
                    }*/
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            Divider(thickness = 0.5.dp, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            val sumOfIncome = (dateInfo?.history?.spendList?.earnList ?: emptyList()).sumOf { it.price }
            Text(
                text = "수입 합계 : ${String.format("%,d", sumOfIncome.toString().toLong())} ￦",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            val sumOfSpend = (dateInfo?.history?.spendList?.spendList ?: emptyList()).sumOf { it.price }
            Text(
                text = "지출 합계 : ${String.format("%,d", sumOfSpend.toString().toLong())} ￦",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.padding(all = 7.dp),
                verticalAlignment = Alignment.CenterVertically) {

                Spacer(modifier = Modifier.weight(1f))

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
                IconButton(
                    onClick = {
                        val history = dateInfo?.history
                        if (history != null) {
                            Log.e("@@@테스트 알림", "삭제 완료!")
                            postViewModel.deleteHistory(history)
                            Log.e("@@@테스트", "id 정보 : ${dateInfo?.history?.id}")
                        }

                    })
                {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Gray,
                    )
                }
            }

        }

    }
}


