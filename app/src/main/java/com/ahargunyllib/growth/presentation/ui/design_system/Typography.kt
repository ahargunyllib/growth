package com.ahargunyllib.growth.presentation.ui.design_system

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ahargunyllib.growth.R

val Nunito = FontFamily()
val typography = Typography()

sealed class GrowthTypography(val textStyle: TextStyle) {

    // Headings / Titles
    data object HeadingXL : GrowthTypography(
        TextStyle(
            fontFamily = Nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            lineHeight = 36.sp
        )
    )

    data object HeadingL : GrowthTypography(
        TextStyle(
            fontFamily = Nunito,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp
        )
    )

    data object HeadingM : GrowthTypography(
        TextStyle(
            fontFamily = Nunito,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
    )

    // Body Texts
    data object BodyL : GrowthTypography(
        TextStyle(
            fontFamily = Nunito,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    )

    data object BodyM : GrowthTypography(
        TextStyle(
            fontFamily = Nunito,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    )

    // Labels / Captions
    data object LabelL : GrowthTypography(
        TextStyle(
            fontFamily = Nunito,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    )
}