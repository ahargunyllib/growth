package com.ahargunyllib.growth.presentation.ui.navigation.nav_obj

sealed class RootNavObj (val route: String){
    data object Authenticated: RootNavObj("authenticated")
    data object Unauthenticated: RootNavObj("unauthenticated")
}