package com.ahargunyllib.growth.presentation.view.unauthenticated

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.RootNavObj
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.UnauthenticatedNavObj
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    unauthenticatedNavController: NavController,
    rootNavController: NavController,
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 1f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )

    LaunchedEffect(Unit) {
        delay(4000)
        startAnimation = true
        delay(1000)

        val isUserLoggedIn = false // TODO: Replace with actual auth check
        if (isUserLoggedIn) {
            rootNavController.navigate(RootNavObj.Authenticated.route) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            unauthenticatedNavController.navigate(UnauthenticatedNavObj.OnBoarding.route) {
                popUpTo(UnauthenticatedNavObj.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GrowthScheme.Primary.color)
            .alpha(alpha),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Splash Screen Logo"
        )
    }
}

@Preview
@Composable
fun SplashPreview() {
    SplashScreen(
        unauthenticatedNavController = rememberNavController(),
        rootNavController = rememberNavController()
    )
}