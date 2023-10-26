package com.farmer.olive.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.farmer.olive.R

val nanum = FontFamily(
    Font(R.font.nanum_square_round_oftb, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.nanum_square_round_ofteb, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.nanum_square_round_oftl, FontWeight.Light, FontStyle.Normal),
    Font(R.font.nanum_square_round_oftr, FontWeight.Normal, FontStyle.Normal),
)


// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = nanum,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

