package com.ahargunyllib.growth.presentation.view.unauthenticated

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.RootNavObj
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.UnauthenticatedNavObj
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    unauthenticatedNavController: NavController,
    rootNavController: NavController,
) {
    LaunchedEffect(Unit) {
        delay(5000)
        // Check authentication state (e.g., from a ViewModel or repository)
        val isUserLoggedIn = false // TODO: Replace with actual auth check
        if (isUserLoggedIn) {
            rootNavController.navigate(RootNavObj.Authenticated.route) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            unauthenticatedNavController.navigate(UnauthenticatedNavObj.Login.route) {
                popUpTo(UnauthenticatedNavObj.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Splash Screen")
    }
}