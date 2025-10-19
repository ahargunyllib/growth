package com.ahargunyllib.growth.presentation.ui.navigation.nav_host

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.UnauthenticatedNavObj
import com.ahargunyllib.growth.presentation.view.unauthenticated.LoginScreen
import com.ahargunyllib.growth.presentation.view.unauthenticated.RegisterScreen
import com.ahargunyllib.growth.presentation.view.unauthenticated.SplashScreen

@Composable
fun UnauthenticatedNavHost(
    rootNavController: NavController
) {
    val unauthenticatedNavController = rememberNavController()

    Scaffold(

    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = unauthenticatedNavController,
            startDestination = UnauthenticatedNavObj.Splash.route,
            builder = {
                composable(UnauthenticatedNavObj.Splash.route) {
                    SplashScreen(
                        unauthenticatedNavController = unauthenticatedNavController,
                        rootNavController = rootNavController
                    )
                }

                composable(UnauthenticatedNavObj.Login.route) {
                    LoginScreen(
                        unauthenticatedNavController = unauthenticatedNavController,
                        rootNavController = rootNavController
                    )
                }

                composable(UnauthenticatedNavObj.Register.route) {
                    RegisterScreen(unauthenticatedNavController = unauthenticatedNavController)
                }
            }
        )
    }
}