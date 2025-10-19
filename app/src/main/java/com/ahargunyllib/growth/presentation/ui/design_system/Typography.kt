package com.ahargunyllib.growth.presentation.ui.design_system

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle

val typography = Typography()

sealed class GrowthTypography(val textStyle: TextStyle){
    // TODO: Implement all the typography
    data object Heading: GrowthTypography(TextStyle())
}