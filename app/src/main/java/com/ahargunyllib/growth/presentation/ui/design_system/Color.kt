package com.ahargunyllib.growth.presentation.ui.design_system

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val lightColorScheme = lightColorScheme(
    primary = Color(0xFF318C87),
    secondary = Color(0xFF519E96),
    tertiary = Color(0xFFF5A623),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
    error = Color(0xFFD32F2F),
)
val darkColorScheme = darkColorScheme(
    primary = Color(0xFF519E96),
    secondary = Color(0xFF318C87),
    tertiary = Color(0xFFF5A623),
    background = Color(0xFF2C3E50),
    surface = Color(0xFF4F4F4F),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
)

sealed class GrowthScheme(val color: Color) {
    // Warna utama sesuai style guide Growth
    data object Primary : GrowthScheme(Color(0xFF318C87))     // Hijau tua
    data object Secondary : GrowthScheme(Color(0xFF519E96))   // Hijau muda
    data object Tertiary : GrowthScheme(Color(0xFFF5A623))    // Oranye (aksen)
    data object Quaternary : GrowthScheme(Color(0xFF2C3E50))  // Biru gelap

    // Warna tambahan
    data object White : GrowthScheme(Color(0xFFFFFFFF))
    data object Black : GrowthScheme(Color(0xFF000000))
    data object GrayLight : GrowthScheme(Color(0xFFF5F5F5))
    data object GrayDark : GrowthScheme(Color(0xFF4F4F4F))
    data object Error : GrowthScheme(Color(0xFFD32F2F))
}

