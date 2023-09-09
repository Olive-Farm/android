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
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
                    onNextClick = viewModel::moveToNextMonth,
                    settingsActivityNavigator = viewModel.settingsActivityNavigator
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
    settingsActivityNavigator: SettingsActivityNavigator
) {
    val isNext = remember { mutableStateOf(true) }
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
            NextMonthIconButton(
                modifier = Modifier.wrapContentSize(),
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Week",
                onClick = {
                    isNext.value = false
                    onPreviousClick()
                }
            )

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
                    modifier = Modifier.width(170.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 5.em,
                    textAlign = TextAlign.Center
                )
            }

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
        IconButton(onClick = {
            context.startActivity(settingsActivityNavigator.getIntent(context))
        }) {
            Icon(Icons.Default.Settings, "Settings icon")
        }

    }
    Divider(modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp), color = Color(0xFF355A1E)
    , thickness = 2.dp)

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
