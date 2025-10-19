package com.ahargunyllib.growth.presentation.ui.design_system

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun Theme(
    content: @Composable () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
) {
    val context = LocalContext.current
    val colorScheme = if (dynamicColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (isDarkTheme) darkColorScheme else lightColorScheme
        }
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