package com.ahargunyllib.growth.presentation.view.unauthenticated

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.presentation.ui.navigation.nav_host.UnauthenticatedNavHost
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.UnauthenticatedNavObj
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    unauthenticatedNavController: NavController = rememberNavController(),
    rootNavController: NavController = rememberNavController(),
){
    LaunchedEffect(keys = emptyArray()) {
        delay(5000)
        // if user login go authenticated screen
        // else go to login screen
        unauthenticatedNavController.navigate(UnauthenticatedNavObj.Login.route)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ){
        Text(text = "Splash Screen")
    }
}