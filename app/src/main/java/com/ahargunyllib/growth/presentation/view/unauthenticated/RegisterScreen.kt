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
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.UnauthenticatedNavObj

@Composable
fun RegisterScreen(
    unauthenticatedNavController: NavController,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(text = "Register Screen")
        Button(
            onClick = {
                unauthenticatedNavController.navigate(UnauthenticatedNavObj.Login.route)
            }
        ) {
            Text(text = "Login")
        }
    }
}