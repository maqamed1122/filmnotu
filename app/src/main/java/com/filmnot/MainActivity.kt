package com.filmnot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.filmnot.navigation.Screen
import com.filmnot.ui.auth.AuthScreen
import com.filmnot.ui.auth.AuthViewModel
import com.filmnot.ui.detail.DetailScreen
import com.filmnot.ui.home.HomeScreen
import com.filmnot.ui.search.SearchScreen
import com.filmnot.ui.settings.SettingsScreen
import com.filmnot.ui.theme.*
import com.filmnot.ui.watchlist.WatchlistScreen
import dagger.hilt.android.AndroidEntryPoint

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            FilmnotTheme {
                FilmnotApp()
            }
        }
    }
}

@Composable
fun FilmnotApp() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    if (authState == null) {
        AuthScreen(onAuthSuccess = {})
    } else {
        MainNavigation(authViewModel = authViewModel)
    }
}

@Composable
fun MainNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home, "Ana Səhifə", Icons.Outlined.Home, Icons.Filled.Home),
        BottomNavItem(Screen.Search, "Axtar", Icons.Outlined.Search, Icons.Filled.Search),
        BottomNavItem(Screen.Watchlist, "Siyahı", Icons.Outlined.BookmarkBorder, Icons.Filled.Bookmarks),
        BottomNavItem(Screen.Settings, "Tənzim", Icons.Outlined.Settings, Icons.Filled.Settings),
    )

    val showBottomBar = currentDestination?.route?.startsWith("detail") == false

    Scaffold(
        containerColor = Background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Surface,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = Surface
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Background)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(onMovieClick = { type, id ->
                    navController.navigate(Screen.MovieDetail.createRoute(type, id))
                })
            }
            composable(Screen.Search.route) {
                SearchScreen(onMovieClick = { type, id ->
                    navController.navigate(Screen.MovieDetail.createRoute(type, id))
                })
            }
            composable(Screen.Watchlist.route) {
                WatchlistScreen(onMovieClick = { type, id ->
                    navController.navigate(Screen.MovieDetail.createRoute(type, id))
                })
            }
            composable(Screen.Settings.route) {
                SettingsScreen(onSignOut = { authViewModel.signOut() })
            }
            composable(
                route = Screen.MovieDetail.route,
                arguments = listOf(
                    navArgument("mediaType") { type = NavType.StringType },
                    navArgument("id") { type = NavType.IntType }
                )
            ) {
                DetailScreen(
                    onBack = { navController.popBackStack() },
                    onMovieClick = { type, id ->
                        navController.navigate(Screen.MovieDetail.createRoute(type, id))
                    }
                )
            }
        }
    }
}
