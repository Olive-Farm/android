package com.farmer.olive.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.farmer.home.ui.TempCalendarViewModel
import com.farmer.olive.navigation.OliveNavHost
import com.farmer.olive.ui.theme.Green500
import com.farmer.olive.ui.theme.OliveTheme

@Composable
fun OliveMain(
    composeNavigator: ComposeNavigator,
    viewModel: MainViewModel = hiltViewModel(),
//    calendarViewModel: CalendarViewModel = hiltViewModel(),
    tempViewModel: TempCalendarViewModel = hiltViewModel()
) {
    OliveTheme {
        val navHostController = rememberNavController()

        // show add dialog if needed
        // todo 옮기기
//        if (calendarViewModel.showDialog.value) {
//            Dialog(
//                onDismissRequest = { calendarViewModel.showDialog.value = false },
//                properties = DialogProperties(
//                    dismissOnBackPress = true,
//                    dismissOnClickOutside = true
//                )
//            ) {
//                Surface(
//                    modifier = Modifier.height(460.dp),
//                    shape = RoundedCornerShape(8.dp),
//                    color = Color.White
//                ) {
//                    AddCashDialog()
//                }
//            }
//        }

        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                BottomAppBar(
                    cutoutShape = CircleShape,
                    backgroundColor = Color.White
                ) {
                    OliveBottomNavigation(navHostController, viewModel.oliveScreens)
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = Green500,
                    onClick = {
                        tempViewModel.setShowPostDialog(true)
//                        calendarViewModel.showDialog.value = true
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            },
            isFloatingActionButtonDocked = true
        ) { _ ->
            OliveNavHost(navHostController = navHostController, composeNavigator = composeNavigator)
        }
    }
}
