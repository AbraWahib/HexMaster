package com.abra.HexMaster.presentation.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home_screen")
    object EasyLevel: Screen("easy_level")
    object MediumLevel: Screen("medium_level")
    object HardLevel: Screen("hard_level")
}