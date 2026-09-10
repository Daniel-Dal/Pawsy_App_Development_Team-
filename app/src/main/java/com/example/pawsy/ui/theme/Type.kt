package com.example.pawsy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.example.pawsy.R

// Set of Material typography styles to start with

val PawsyFontFamily = FontFamily(
    Font(R.font.poppins_regular),
    Font(R.font.poppins_bold)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = PawsyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PawsyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 37.sp,
        lineHeight = 40.sp,
        letterSpacing = 1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PawsyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 10.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = PawsyFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 10.sp,
        letterSpacing = 0.5.sp
    )

)