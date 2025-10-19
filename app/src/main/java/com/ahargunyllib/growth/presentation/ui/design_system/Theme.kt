package com.ahargunyllib.growth.presentation.ui.design_system

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun Theme(
    content: @Composable () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
) {
    val colorScheme = if (dynamicColor) {
        if (isDarkTheme) darkColorScheme else lightColorScheme
    } else {
        if (isDarkTheme) darkColorScheme else lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
        typography = typography,
        shapes = shapes
    )
}