package com.abra.HexMaster.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigator() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(route = Screen.Home.route) {
            // HomeScreen(navController = navController)
        }
        composable(route = Screen.EasyLevel.route) {
            // EasyLevelScreen(navController = navController)
        }
        composable(route = Screen.MediumLevel.route) {
            // MediumLevelScreen(navController = navController)
        }
        composable(route = Screen.HardLevel.route) {
            //HardLevelScreen(navController = navController)
        }

    }
}