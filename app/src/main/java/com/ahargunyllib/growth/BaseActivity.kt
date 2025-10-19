package com.ahargunyllib.growth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.ahargunyllib.growth.presentation.ui.design_system.Theme
import com.ahargunyllib.growth.presentation.ui.navigation.nav_host.RootNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Theme(
                isDarkTheme = false,
                dynamicColor = false,
                content = {
                    RootNavHost()
                },
            )
        }
    }
}
