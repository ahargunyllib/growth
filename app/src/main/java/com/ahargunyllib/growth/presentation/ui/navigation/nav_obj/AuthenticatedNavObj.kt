package com.ahargunyllib.growth.presentation.ui.navigation.nav_obj

sealed class AuthenticatedNavObj(val route: String) {
    data object HomeScreen: AuthenticatedNavObj("home")
    data object MapsScreen: AuthenticatedNavObj("maps")
    data object ProfileScreen: AuthenticatedNavObj("profile")

    data object ExchangePoint: AuthenticatedNavObj("exchange_point")
    data object SuccessExchange : AuthenticatedNavObj("success_exchange_screen/{points}/{amount}") {
        fun createRoute(points: Int, amount: Int) = "success_exchange_screen/$points/$amount"
    }
    data object ScanQR: AuthenticatedNavObj("scan_qr")
    data object SuccessDeposit : AuthenticatedNavObj("success_deposit_screen/{points}/{weight}") {
        fun createRoute(points: Int, weight: Float) = "success_deposit_screen/$points/$weight"
    }
    data object AchievementScreen: AuthenticatedNavObj("achievement")
    data object ClaimAchievement: AuthenticatedNavObj("claim_achievement/{missionId}") {
        fun createRoute(missionId: String) = "claim_achievement/$missionId"
    }
    data object SuccessAchievement: AuthenticatedNavObj("success_achievement/{points}") {
        fun createRoute(points: Int) = "success_achievement/$points"
    }
    data object HistoryDepositScreen : AuthenticatedNavObj("history_deposit_screen")
}