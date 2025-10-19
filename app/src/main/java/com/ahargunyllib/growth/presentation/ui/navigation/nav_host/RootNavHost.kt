package com.ahargunyllib.growth.presentation.ui.navigation.nav_host

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.RootNavObj

@Composable
fun RootNavHost() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = RootNavObj.Unauthenticated.route,
        builder = {
            composable(RootNavObj.Unauthenticated.route) {
                UnauthenticatedNavHost(rootNavController)
            }

            composable(
                route = RootNavObj.Authenticated.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left, tween(700)
                    )
                }, popExitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                    )
                },
                content = {
                    AuthenticatedNavHost(
                        rootNavController
                    )
                }
            )
        },
    )
}