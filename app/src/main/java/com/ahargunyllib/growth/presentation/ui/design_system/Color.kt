package com.ahargunyllib.growth.presentation.ui.design_system

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val lightColorScheme = lightColorScheme()
val darkColorScheme = darkColorScheme()

sealed class GrowthScheme(val color: Color) {

    data object Primary : GrowthScheme(Color(0xFF318C87))        // Hijau tua
    data object Secondary : GrowthScheme(Color(0xFF519E96)) // Hijau muda
    data object Fourth : GrowthScheme(Color(0xFFF5A623))      // Oranye aksen
    data object Third : GrowthScheme(Color(0xFF2C3E50))       // Biru gelap

    data object White : GrowthScheme(Color(0xFFFFFFFF))
    data object Black : GrowthScheme(Color(0xFF000000))
    data object Background : GrowthScheme(Color(0xFFF5F6F7))
    data object Disabled : GrowthScheme(Color(0xFFCACACA))
    data object Black2 : GrowthScheme(Color(0xFF3D3D3D))
    data object Error : GrowthScheme(Color(0xFFD84C4C))
}

