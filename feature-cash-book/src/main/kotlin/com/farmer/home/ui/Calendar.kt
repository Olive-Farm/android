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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.home.ui.detail.DetailDialogByState
import kotlinx.datetime.Month
import java.time.format.TextStyle
import java.util.*

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    viewmodel: TempCalendarViewModel = hiltViewModel()
) {
    // todo isaac collectAsStateWithLifecycle로 바꾸고 싶은데 안되고 있음.
    val uiState: CalendarUiState by viewmodel.calendarUiState.collectAsState()
    val dialogUiState: DialogUiState by viewmodel.dialogUiState.collectAsState()
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
                    onPreviousClick = viewmodel::moveToPreviousMonth,
                    onNextClick = viewmodel::moveToNextMonth
                )

                CalendarDates(state)
            }
        }
        else -> {}
    }
    when (val state = dialogUiState) {
        is DialogUiState.DetailDialog -> {
            DetailDialogByState(state)
        }
        is DialogUiState.PostDialog -> {

        }
        else -> {}
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CalendarHeader(
    modifier: Modifier,
    month: Month,
    year: Int,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    val isNext = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 8.dp)
    ) {
        // calendar title (Ex. January 2022)
        AnimatedContent(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .align(Alignment.CenterVertically),
            targetState = getDateTitleText(month, year),
            transitionSpec = {
                addSlideAnimation(isNext = isNext.value).using(
                    SizeTransform(clip = false)
                )
            }
        ) {
            Text(
                text = it,
                modifier = Modifier,
                fontWeight = FontWeight.Bold
            )
        }

        // previous, next month icon button
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.End
        ) {
            NextMonthIconButton(
                modifier = Modifier.wrapContentSize(),
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Week",
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
    }
}

private fun getDateTitleText(month: Month, year: Int): String {
    return month.getDisplayName(TextStyle.FULL, Locale.getDefault()).lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
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
