package com.farmer.olive.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.farmer.olive.navigation.OliveNavHost
import com.farmer.olive.ui.addcash.AddCashDialog
import com.farmer.olive.ui.theme.Green500
import com.farmer.olive.ui.theme.OliveTheme

@Composable
fun OliveMain(
    composeNavigator: ComposeNavigator,
    viewModel: MainViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    OliveTheme {
        val navHostController = rememberNavController()

        // show add dialog if needed
        if (showDialog) {
            Dialog(
                onDismissRequest = {
                    showDialog = !showDialog
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    modifier = Modifier.height(460.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ) {
                    AddCashDialog()
                }
            }
        }

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
                    onClick = { showDialog = !showDialog }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            },
            isFloatingActionButtonDocked = true
        ) {
            OliveNavHost(navHostController = navHostController, composeNavigator = composeNavigator)
        }
    }
}
