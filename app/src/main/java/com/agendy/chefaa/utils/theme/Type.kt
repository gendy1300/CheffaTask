package com.agendy.chefaa.utils.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.agendy.chefaa.R

// Set of Material typography styles to start with
val poppinsFonts = FontFamily(
    Font(R.font.poppins_regular, weight = FontWeight.Normal),
    Font(R.font.poppins_medium, weight = FontWeight.Medium),
    Font(R.font.poppins_semibold, weight = FontWeight.SemiBold),
    Font(R.font.poppins_light, weight = FontWeight.Light),
    Font(R.font.poppins_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.poppins_thin, weight = FontWeight.Thin),
    Font(R.font.poppins_extrabold, weight = FontWeight.ExtraBold),
)

val italicPoppinsFonts = FontFamily(
    Font(R.font.poppins_italic, weight = FontWeight.Normal),
)

val poppinsFont = Typography(
    titleLarge = TextStyle(
        fontFamily = poppinsFonts,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    )
)

val poppinsItalicFont = Typography(
    TextStyle(
        fontFamily = italicPoppinsFonts,
        fontWeight = FontWeight.Normal,
    )
)