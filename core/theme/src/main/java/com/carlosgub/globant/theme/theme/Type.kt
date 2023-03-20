package com.carlosgub.globant.theme.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    h4 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Medium,
        color = TextColor,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        color = TextColor,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        color = TextColor,
        fontStyle = FontStyle.Italic,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        color = Color.White
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        color = TextColor,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = FontFamily.Default,
        color = TextColor,
        fontSize = 14.sp
    )
)

val TextFieldStyle =
    TextStyle(
        color = TextColor,
        fontFamily = FontFamily.Default,
        fontSize = 16.sp
    )

val OutlineTextFieldStyleLabel =
    TextStyle(
        color = TextColor,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Light,
        fontFamily = FontFamily.Default,
        fontSize = 12.sp
    )

val OutlineTextFieldStylePlaceholder =
    TextStyle(
        color = TextColor,
        fontFamily = FontFamily.Default,
        fontSize = 16.sp
    )