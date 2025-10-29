package com.ahargunyllib.growth.presentation.ui.navigation.nav_host

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import com.ahargunyllib.growth.presentation.ui.widget.common.Navbar
import com.ahargunyllib.growth.presentation.view.authenticated.AchievementScreen
import com.ahargunyllib.growth.presentation.view.authenticated.ClaimAchievementScreen
import com.ahargunyllib.growth.presentation.view.authenticated.ExchangePointScreen
import com.ahargunyllib.growth.presentation.view.authenticated.HomeScreen
import com.ahargunyllib.growth.presentation.view.authenticated.MapsScreen
import com.ahargunyllib.growth.presentation.view.authenticated.ProfileScreen
import com.ahargunyllib.growth.presentation.view.authenticated.ScanQRScreen
import com.ahargunyllib.growth.presentation.view.authenticated.SuccessAchievementScreen
import com.ahargunyllib.growth.presentation.view.authenticated.SuccessDepositScreen
import com.ahargunyllib.growth.presentation.viewmodel.NavbarViewModel

@Composable
fun AuthenticatedNavHost(rootNavController: NavController) {
    val authenticatedNavController = rememberNavController()

    val navbarViewModel = hiltViewModel<NavbarViewModel>()

    Scaffold(
        bottomBar = {
            Navbar(
                authenticatedNavController = authenticatedNavController,
                navbarViewModel = navbarViewModel
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = authenticatedNavController,
            startDestination = AuthenticatedNavObj.HomeScreen.route,
            builder = {
                composable(
                    route = AuthenticatedNavObj.HomeScreen.route,
                    content = {
                        HomeScreen(
                            authenticatedNavController = authenticatedNavController,
                        )
                    }
                )

                composable(
                    route = AuthenticatedNavObj.MapsScreen.route,
                    content = {
                        MapsScreen(
                            authenticatedNavController = authenticatedNavController,
                        )
                    }
                )

                composable(
                    route = AuthenticatedNavObj.ProfileScreen.route,
                    content = {
                        ProfileScreen(
                            authenticatedNavController = authenticatedNavController,
                            rootNavController = rootNavController,
                        )
                    }
                )
                composable(
                    route = AuthenticatedNavObj.ExchangePoint.route,
                    content = {
                        ExchangePointScreen(
                            authenticatedNavController = authenticatedNavController,
                            rootNavController = rootNavController,
                        )
                    }
                )
                composable(
                    route = AuthenticatedNavObj.ScanQR.route,
                    content = {
                        ScanQRScreen(
                            authenticatedNavController = authenticatedNavController,
                        )
                    }
                )

                composable(
                    route = AuthenticatedNavObj.SuccessDeposit.route,
                    arguments = listOf(
                        navArgument("points") { type = NavType.FloatType },
                        navArgument("weight") { type = NavType.FloatType }
                    ),
                    content = { backStackEntry ->
                        val points = backStackEntry.arguments?.getFloat("points") ?: 0F
                        val weight = backStackEntry.arguments?.getFloat("weight") ?: 0F
                        SuccessDepositScreen(
                            authenticatedNavController = authenticatedNavController,
                            points = points,
                            weight = weight
                        )
                    }
                )
                
                composable(
                    route = AuthenticatedNavObj.AchievementScreen.route,
                    content = {
                        AchievementScreen(
                            authenticatedNavController = authenticatedNavController,
                            rootNavController = rootNavController,
                        )
                    }
                )

                composable(
                    route = AuthenticatedNavObj.ClaimAchievement.route,
                    arguments = listOf(navArgument("missionId") { type = NavType.StringType })
                ) { backStackEntry ->
                    // Ekstrak missionId dari argumen
                    val missionId = backStackEntry.arguments?.getString("missionId")

                    // Panggil ClaimAchievementScreen dengan missionId
                    ClaimAchievementScreen(
                        authenticatedNavController = authenticatedNavController,
                        missionId = missionId
                    )
               }

                composable(
                    route = AuthenticatedNavObj.SuccessAchievement.route,
                    arguments = listOf(navArgument("points") { type = NavType.IntType })
                ) { backStackEntry ->
                    val points = backStackEntry.arguments?.getInt("points")
                    SuccessAchievementScreen(
                        authenticatedNavController = authenticatedNavController,
                        points = points
                    )
                }
            }
        )
    }
}