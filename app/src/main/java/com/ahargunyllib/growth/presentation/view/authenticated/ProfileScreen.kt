package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.RootNavObj

@Composable
fun ProfileScreen(
    authenticatedNavController: NavController = rememberNavController(),
    rootNavController: NavController = rememberNavController(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Profile Screen")
        Button(
            onClick = {
                rootNavController.navigate(RootNavObj.Unauthenticated.route)
            }
        ) {
            Text(text = "Logout")
        }
    }
}