package com.farmer.feature_settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.Surface
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun EditCategoryDialog(onDismissRequest: () -> Unit
){
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ){
        Surface(
            modifier = Modifier.height(500.dp)
                .shadow(12.dp, shape = RoundedCornerShape(8.dp)),             //어느 날부터 뒷배경이 흐리게 안 됨.. 고치고싶은데 진짜 안 됨..
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ){
            EditCategory(onDismissRequest)
        }
    }
}