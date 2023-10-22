package com.example.feature_post

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

@Composable
fun PostDialog(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier.height(450.dp)
                .shadow(12.dp, shape = RoundedCornerShape(8.dp)),             //어느 날부터 뒷배경이 흐리게 안 됨.. 고치고싶은데 진짜 안 됨..
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            AddCash(onDismissRequest)
        }
    }
}
