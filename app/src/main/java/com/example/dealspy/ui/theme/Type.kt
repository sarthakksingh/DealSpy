package com.example.dealspy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.dealspy.R


val PoppinsFamily = FontFamily(
    Font(R.font. poppins_regular, FontWeight.Normal),
    Font(R.font. poppins_medium, FontWeight.Medium),
    Font(R.font. poppins_semibold, FontWeight.SemiBold),
    Font(R.font. poppins_bold, FontWeight.Bold),
    Font(R.font. poppins_light, FontWeight.Light),
    Font(R.font. poppins_thin, FontWeight.Thin)
)

// Bold displayMedium
val Typography = Typography(
    displayMedium = TextStyle(
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.02.sp
    ),

    // Normal bodyMedium
    bodyMedium = TextStyle(
        fontFamily =  PoppinsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.065.sp
    ),


    // Normal displaySmall
    displaySmall = TextStyle(
        fontFamily =  PoppinsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.1.sp
    ),

    // SemiBold displayNormal
    titleSmall = TextStyle(
        fontFamily =  PoppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.1.sp
    ),

    // Medium headlineSmall
    headlineSmall = TextStyle(
        fontFamily =  PoppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.1.sp
    ),

    // Medium headlineMedium
    headlineMedium = TextStyle(
        fontFamily =  PoppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),

    // Normal labelMedium
    labelMedium = TextStyle(
        fontFamily =  PoppinsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.02.sp
    ),

    labelSmall = TextStyle(
        fontFamily =  PoppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.02.sp
    ),
)