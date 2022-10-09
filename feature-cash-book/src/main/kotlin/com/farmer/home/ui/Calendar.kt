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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.time.format.TextStyle
import java.util.*

@Composable
fun Calendar(
    modifier: Modifier = Modifier
) {
    val currentDay = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val displayMonth = remember {
        mutableStateOf(currentDay.month)
    }
    val (currentMonth, currentYear) = displayMonth.value to currentDay.year
    val daysInMonth = currentMonth.minLength() // 해당 달이 총 며칠인지
    val year = currentDay.year

    Column(
        modifier = modifier
            .background(Color.Green)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        CalendarHeader(
            modifier = Modifier,
            month = displayMonth.value,
            year = year,
            onPreviousClick = {
                displayMonth.value = displayMonth.value.minus(1)
            },
            onNextClick = {
                displayMonth.value = displayMonth.value.plus(1)
            }
        )
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
    arrowShown: Boolean = true
) {
    val isNext = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        AnimatedContent(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .align(Alignment.CenterVertically),
            targetState = getTitleText(month, year),
            transitionSpec = {
                addAnimation(isNext = isNext.value).using(
                    SizeTransform(clip = false)
                )
            }
        ) {
            Text(
                text = it,
                modifier = Modifier
            )
        }

        if (arrowShown) {
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
}

private fun getTitleText(month: Month, year: Int): String {
    return month.getDisplayName(TextStyle.FULL, Locale.getDefault()).lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    } + " " + year
}

@OptIn(ExperimentalAnimationApi::class)
private fun addAnimation(duration: Int = 500, isNext: Boolean): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) height else -height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) -height else height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}
