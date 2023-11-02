package com.farmer.home.ui.detail

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.feature_post.AddCash
import com.example.feature_post.EditCash
import com.example.feature_post.PostDialog
import com.farmer.data.DateInfo
import com.farmer.data.History
import com.farmer.home.ui.BlueAlpha200
import com.farmer.home.ui.RedAlpha200
import com.farmer.home.ui.TempCalendarViewModel
import com.farmer.home.ui.postdialog.PostDialogViewModel
import com.farmer.home.ui.states.CalendarViewModel

@Composable
fun DetailDialog(
    dateInfo: DateInfo?,
    isDialogEditMode: Boolean,
    viewModel: CalendarViewModel = hiltViewModel(),
    postViewModel: PostDialogViewModel = hiltViewModel()
) {
    val deletedTransactionId by postViewModel.deletedId.collectAsState()
    val uiState = viewModel.viewModelState.collectAsState()

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
        Divider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFF537150))

        if (dateInfo?.history?.spendList?.earnList.isNullOrEmpty() && dateInfo?.history?.spendList?.spendList.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF537150)
                            )
                        ) {
                            append("OliveBook")
                        }
                        withStyle(style = SpanStyle(color = Color.DarkGray)) {
                            append("을 이용하여\n 편리하게 가계부를 관리해보세요")
                        }
                    }, fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            //if ()
                LazyColumn {
                    itemsIndexed(
                        dateInfo?.history?.spendList?.earnList?.filter { transactData ->
                            deletedTransactionId.find { it == transactData.id } == null
                        } ?: emptyList()
                    ) { index, incomeData ->
                        TransactItem(
                            index = index,
                            isSpend = false,
                            spendData = incomeData,
                            isDialogEditMode = isDialogEditMode,
                            onDeleteItem = {
                                postViewModel.deleteTransactionData(
                                    historyId = dateInfo?.history?.id,
                                    transactionId = incomeData.id
                                )
                            },
                            dateInfo = dateInfo
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    itemsIndexed(
                        dateInfo?.history?.spendList?.spendList?.filter { transactData ->
                            deletedTransactionId.find { it == transactData.id } == null
                        } ?: emptyList()
                    ) { index, spendData ->
                        TransactItem(
                            index = index,
                            isSpend = true,
                            spendData = spendData,
                            isDialogEditMode = isDialogEditMode,
                            onDeleteItem = {
                                postViewModel.deleteTransactionData(
                                    historyId = dateInfo?.history?.id,
                                    transactionId = spendData.id
                                )
                            },
                            dateInfo = dateInfo
                            //테스팅중
                            /*onEditItem = {
                                postViewModel.
                            }*/
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }



            Spacer(modifier = Modifier.height(12.dp))

            Divider(thickness = 0.5.dp, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            val sumOfIncome =
                (dateInfo?.history?.spendList?.earnList ?: emptyList()).sumOf { it.price }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "수입 합계",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF355A1E)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = String.format("%,d", sumOfIncome.toString().toLong()),
                    color = BlueAlpha200,
                    fontWeight = FontWeight.Bold
                )
                Text(text = " ￦")
            }

            Spacer(modifier = Modifier.height(6.dp))

            val sumOfSpend =
                (dateInfo?.history?.spendList?.spendList ?: emptyList()).sumOf { it.price }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "지출 합계",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF355A1E)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = String.format("%,d", sumOfSpend.toString().toLong()),
                    color = RedAlpha200,
                    fontWeight = FontWeight.Bold
                )
                Text(text = " ￦")
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(all = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

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

                /*IconButton(onClick = { viewModel.changeEditModeState() }) {
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
                }*/
            }

        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactItem(
    index: Int,
    isSpend: Boolean,
    spendData: History.Transact.TransactData,
    onDeleteItem: () -> Unit,
    isDialogEditMode: Boolean,
    viewModel: TempCalendarViewModel = hiltViewModel(),
    postViewModel: CalendarViewModel = hiltViewModel(),
    dateInfo: DateInfo?
) {
    val roundedCornerShape = RoundedCornerShape(10.dp)
    var showDialog by remember { mutableStateOf(false) }
    val dismissState = rememberDismissState(confirmStateChange = { dismissValue ->
        when (dismissValue) {
            DismissValue.Default -> { // dismissThresholds 만족 안한 상태
                false
            }

            DismissValue.DismissedToEnd -> { // -> 방향 스와이프 (수정)
                showDialog = true
                true
            }

            DismissValue.DismissedToStart -> { // <- 방향 스와이프 (삭제)
                onDeleteItem.invoke()
                true
            }
        }
    })

    if (showDialog) {
        EditCash(dateInfo, isSpend, spendData, onDismissRequest = { viewModel.setShowPostDialog(false) })
    }
    else {
    SwipeToDismiss(
        modifier = Modifier.clip(roundedCornerShape),
        state = dismissState,
        dismissThresholds = { FractionalThreshold(0.25f) },
        background = { // 기존에 보일 항목
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> backgroundColor.copy(alpha = 0.5f) // dismissThresholds 만족 안한 상태
                    DismissValue.DismissedToEnd -> Color(0xFF52ACFF).copy(alpha = 0.5f) // -> 방향 스와이프 (수정) //파란색으로 변경
                    DismissValue.DismissedToStart -> Color(0xFFFF95CE).copy(alpha = 0.5f) // <- 방향 스와이프 (삭제)
                }, label = ""
            )
            val alignment = when (direction) {
                DismissDirection.EndToStart -> Alignment.CenterEnd
                DismissDirection.StartToEnd -> Alignment.CenterStart
            }
            val icon = when (dismissState.targetValue) {
                DismissValue.Default -> null
                DismissValue.DismissedToEnd -> Icons.Default.Edit
                DismissValue.DismissedToStart -> Icons.Default.Delete
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 30.dp),
                contentAlignment = alignment
            ) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }

        },
        dismissContent = { // 지워지면 보일 항목
            TransactContent(index, spendData, isDialogEditMode, isSpend)
        })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactContent(
    index: Int,
    spendData: History.Transact.TransactData,
    isDialogEditMode: Boolean,
    isSpend: Boolean
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(Color.White)
    ) {
        val titleVisibility = if (index == 0) 1f else 0f

        Text(
            text = if (isSpend) {
                "지출"
            } else {
                "수입"
            },
            modifier = Modifier.alpha(titleVisibility),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = spendData.item)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = String.format("%,d", spendData.price.toString().toLong()),
            //color = RedAlpha200
            fontWeight = FontWeight.SemiBold
        )

        Text(text = " ￦")

        if (isDialogEditMode) {
            IconButton(
                modifier = Modifier
                    .padding(0.dp)
                    .size(16.dp),
                onClick = { /* todo */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Gray,
                )

            }
        }
    }
}
