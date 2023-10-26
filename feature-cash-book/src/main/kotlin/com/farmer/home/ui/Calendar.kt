package com.farmer.home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature_post.PostDialog
import com.farmer.feature_settings.EditCategoryDialog
import com.farmer.home.ui.detail.DetailDialogByState
import com.farmer.navigator.SettingsActivityNavigator
import kotlinx.datetime.Month
import java.time.format.TextStyle
import java.util.*

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    viewModel: TempCalendarViewModel = hiltViewModel()
) {
    // todo isaac collectAsStateWithLifecycle로 바꾸고 싶은데 안되고 있음.
    val uiState: CalendarUiState by viewModel.calendarUiState.collectAsState()
    val dialogUiState: DialogUiState by viewModel.dialogUiState.collectAsState()
    when (val state = uiState) {
        is CalendarUiState.Success -> {
            Column(
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                CalendarHeader(
                    modifier = Modifier,
                    month = state.dateViewInfo.last().dateInfo?.date?.month ?: Month.JANUARY,
                    year = state.dateViewInfo.last().dateInfo?.date?.year ?: -1,
                    onPreviousClick = viewModel::moveToPreviousMonth,
                    onNextClick = viewModel::moveToNextMonth
                    //settingsActivityNavigator = viewModel.settingsActivityNavigator
                )

                CalendarDates(state)
            }
        }

        else -> Unit
    }
    when (val state = dialogUiState) {
        is DialogUiState.DetailDialog -> DetailDialogByState(state)
        is DialogUiState.PostDialog ->
            PostDialog(onDismissRequest = { viewModel.setShowPostDialog(false) })
        is DialogUiState.EditCategoryDialog ->
            EditCategoryDialog(onDismissRequest = { viewModel.setShowEditCategoryDialog(false) })
        is DialogUiState.NotShowing -> Unit
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CalendarHeader(
    modifier: Modifier,
    month: Month,
    year: Int,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    viewModel: TempCalendarViewModel = hiltViewModel()
    //settingsActivityNavigator: SettingsActivityNavigator
) {
    val isNext = remember { mutableStateOf(true) }
    val dialogState = viewModel.editCategoryDialogState.collectAsState()

    ShowEditCategoryDialogByState(
        dialogState = dialogState,
        onDismissRequest = {
            viewModel.setShowEditCategoryDialog(false)
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 10.dp, bottom = 10.dp, start = 8.dp)
    ) {

        // previous, next month icon button

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.Start
        ) {


            // calendar title (Ex. January 2022)
            AnimatedContent(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .align(Alignment.CenterVertically),
                targetState = getDateTitleText(year, month),
                transitionSpec = {
                    addSlideAnimation(isNext = isNext.value).using(
                        SizeTransform(clip = false)
                    )
                }, label = ""
            ) {
                val dateArray = it.split(' ')
                Row (modifier = Modifier.width(130.dp)) {
                Text(
                    text = dateArray[1],
                    modifier = Modifier.padding(horizontal = 10.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 5.em,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFDDDDDD),
                )
                Text(
                    text = dateArray[0],
                    fontWeight = FontWeight.Bold,
                    fontSize = 5.em,
                    textAlign = TextAlign.Center,


                )
                }
            }

            NextMonthIconButton(
                modifier = Modifier.wrapContentSize(),
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Month",
                onClick = {
                    isNext.value = false
                    onPreviousClick()
                }
            )

            NextMonthIconButton(
                modifier = Modifier.wrapContentSize(),
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next Month",
                onClick = {
                    isNext.value = true
                    onNextClick()
                }
            )
        }

        Spacer(Modifier.weight(1f))

        val context = LocalContext.current
        var menuExpanded by remember { mutableStateOf(false) }

        Box {
            IconButton(onClick = {
                menuExpanded = true
                //context.startActivity(settingsActivityNavigator.getIntent(context))
            }) {
                Icon(Icons.Default.Settings, "Settings icon")
            }
            DropdownMenu(
                modifier = Modifier.wrapContentSize(),
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ){
                DropdownMenuItem(onClick = {
                    viewModel.setShowEditCategoryDialog(true)
                    //viewModel.setEditCategoryDialog(true)
                    menuExpanded = false
                }) {
                    Row {
                        Icon(
                            Icons.Filled.BorderColor,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 10.dp),
                            tint = Color.DarkGray
                        )
                        Text(text = "카테고리 수정하기",)
                    }
                }
            }
        }

    }

}

private fun getDateTitleText(year: Int, month: Month,): String {
    return month.getDisplayName(TextStyle.FULL, Locale.getDefault()).uppercase().replaceFirstChar {
        if (it.isUpperCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    } + " " + year
}

@OptIn(ExperimentalAnimationApi::class)
private fun addSlideAnimation(duration: Int = 500, isNext: Boolean): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) height else -height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) -height else height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}

@Composable
private fun ShowEditCategoryDialogByState(
    dialogState: State<EditCategoryDialogState>,
    onDismissRequest: () -> Unit
) {
    when (dialogState.value) {
        EditCategoryDialogState.ShowEditDialog -> {
            EditCategoryDialog(onDismissRequest = { onDismissRequest() })
        }

        EditCategoryDialogState.NotShowingEditDialog -> Unit
    }
}