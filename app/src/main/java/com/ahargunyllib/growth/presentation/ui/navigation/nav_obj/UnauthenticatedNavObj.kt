package com.ahargunyllib.growth.presentation.ui.navigation.nav_obj


sealed class UnauthenticatedNavObj(val route: String) {
    data object Splash: UnauthenticatedNavObj("splash")
    data object Login: UnauthenticatedNavObj("login")
    data object Register: UnauthenticatedNavObj("register")
}