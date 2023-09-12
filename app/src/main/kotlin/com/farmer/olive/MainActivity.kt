package com.farmer.olive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.ComposeNavigator
import com.farmer.olive.ui.OliveMain
import com.farmer.olive.ui.theme.OliveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // todo @김규빈 이거 주석 해제하면 버그 때문에 앱 실행이 안되서 일단 주석 처리 해둘게!

        //readSMSMessage(this)
        val composeNavigator = ComposeNavigator()
        setContent {
            OliveMain(composeNavigator)
        }
        //test()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OliveTheme {
        Greeting("Android")
    }
}