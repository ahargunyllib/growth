package com.ahargunyllib.growth.presentation.view.unauthenticated

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.RootNavObj
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.UnauthenticatedNavObj

@Composable
fun LoginScreen(
    unauthenticatedNavController: NavController,
    rootNavController: NavController,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Login Screen")
        Button(
            onClick = {
                rootNavController.navigate(RootNavObj.Authenticated.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        ) {
            Text(text = "Login")
        }
        Button(
            onClick = {
                unauthenticatedNavController.navigate(UnauthenticatedNavObj.Register.route)
            }
        ) {
            Text(text = "Register")
        }
    }
}