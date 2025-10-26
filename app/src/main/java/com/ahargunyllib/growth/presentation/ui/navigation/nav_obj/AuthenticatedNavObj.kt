package com.ahargunyllib.growth.presentation.ui.navigation.nav_obj

sealed class AuthenticatedNavObj(val route: String) {
    data object HomeScreen: AuthenticatedNavObj("home")
    data object MapsScreen: AuthenticatedNavObj("maps")
    data object ProfileScreen: AuthenticatedNavObj("profile")

    data object ExchangePoint: AuthenticatedNavObj("exchange_point")
    data object AchievementScreen: AuthenticatedNavObj("achievement")
}