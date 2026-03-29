package com.filmnot.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Watchlist : Screen("watchlist")
    object Settings : Screen("settings")
    object MovieDetail : Screen("detail/{mediaType}/{id}") {
        fun createRoute(mediaType: String, id: Int) = "detail/$mediaType/$id"
    }
}
