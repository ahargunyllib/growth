package com.ahargunyllib.growth.presentation.ui.design_system

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val lightColorScheme = lightColorScheme()
val darkColorScheme = darkColorScheme()

sealed class GrowthScheme(val color: Color){
    // TODO: Implement all the color
    data object Primary: GrowthScheme(Color(0xFF000000))
}