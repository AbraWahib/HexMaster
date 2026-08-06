package com.abra.hexmaster.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DifficultySelect : Screen("difficulty_select")
    object Game : Screen("game/{difficulty}") {
        fun createRoute(difficulty: String) = "game/$difficulty"
    }
    object GameOver : Screen("game_over/{score}/{streak}/{difficulty}") {
        fun createRoute(score: Int, streak: Int, difficulty: String) = "game_over/$score/$streak/$difficulty"
    }
    object Settings : Screen("settings")
}
