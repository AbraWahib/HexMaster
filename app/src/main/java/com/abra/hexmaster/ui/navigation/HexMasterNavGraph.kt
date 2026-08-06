package com.abra.hexmaster.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.di.AppContainer
import com.abra.hexmaster.ui.screens.difficultyselect.DifficultySelectScreen
import com.abra.hexmaster.ui.screens.game.GameScreen
import com.abra.hexmaster.ui.screens.game.GameViewModel
import com.abra.hexmaster.ui.screens.gameover.GameOverScreen
import com.abra.hexmaster.ui.screens.home.HomeScreen
import com.abra.hexmaster.ui.screens.home.HomeViewModel
import com.abra.hexmaster.ui.screens.settings.SettingsScreen
import com.abra.hexmaster.ui.screens.settings.SettingsViewModel

@Composable
fun HexMasterNavGraph(
    navController: NavHostController,
    appContainer: AppContainer,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.Factory(appContainer.settingsDataStore)
            )
            HomeScreen(
                viewModel = viewModel,
                onPlayClick = { navController.navigate(Screen.DifficultySelect.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.DifficultySelect.route) {
            DifficultySelectScreen(
                onDifficultySelected = { difficulty ->
                    navController.navigate(Screen.Game.createRoute(difficulty.name))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Game.route,
            arguments = listOf(navArgument("difficulty") { type = NavType.StringType })
        ) { backStackEntry ->
            val difficultyName = backStackEntry.arguments?.getString("difficulty") ?: Difficulty.EASY.name
            val difficulty = Difficulty.valueOf(difficultyName)
            
            val gameEngine = appContainer.createGameEngine(difficulty)
            val viewModel: GameViewModel = viewModel(
                factory = GameViewModel.Factory(gameEngine, appContainer.soundManager, appContainer.settingsDataStore),
                key = difficultyName // Key by difficulty to get fresh VM if switching
            )
            
            GameScreen(
                viewModel = viewModel,
                onGameOver = { score, streak, diff ->
                    navController.navigate(Screen.GameOver.createRoute(score, streak, diff)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.GameOver.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("streak") { type = NavType.IntType },
                navArgument("difficulty") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val streak = backStackEntry.arguments?.getInt("streak") ?: 0
            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: Difficulty.EASY.name
            
            GameOverScreen(
                score = score,
                streak = streak,
                difficulty = difficulty,
                onRetrySame = {
                    navController.navigate(Screen.Game.createRoute(difficulty)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onRetryChange = {
                    navController.navigate(Screen.DifficultySelect.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onHome = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }
        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.Factory(appContainer.settingsDataStore)
            )
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
