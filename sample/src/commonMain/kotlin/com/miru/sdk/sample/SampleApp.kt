package com.miru.sdk.sample

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miru.sdk.navigation.getLongArgument
import com.miru.sdk.sample.presentation.SampleRoutes
import com.miru.sdk.sample.presentation.bookmark.BookmarkScreen
import com.miru.sdk.sample.presentation.detail.DetailScreen
import com.miru.sdk.sample.presentation.detail.DetailViewModel
import com.miru.sdk.sample.presentation.home.HomeScreen
import com.miru.sdk.sample.presentation.search.SearchScreen
import com.miru.sdk.sample.presentation.settings.SettingsScreen
import com.miru.sdk.sample.presentation.settings.SettingsViewModel
import com.miru.sdk.ui.components.theme.MiruTheme
import com.miru.sdk.ui.components.theme.darkMiruColors
import com.miru.sdk.ui.components.theme.lightMiruColors
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Bottom navigation tab definitions.
 */
private data class BottomNavTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val bottomNavTabs = listOf(
    BottomNavTab(SampleRoutes.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavTab(SampleRoutes.SEARCH, "Search", Icons.Filled.Search, Icons.Outlined.Search),
    BottomNavTab(SampleRoutes.BOOKMARKS, "Bookmarks", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
    BottomNavTab(SampleRoutes.SETTINGS, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
)

/**
 * Main entry point for the Miru News sample app.
 *
 * Uses BottomNavigation for tab switching (Home, Search, Bookmarks, Settings)
 * and a separate Detail route pushed on top.
 *
 * Settings (dark mode, font size) are observed at this level so they apply
 * to the entire app through [MiruTheme].
 */
@Composable
fun SampleApp() {
    // Observe settings at app level so theme changes apply globally
    val settingsViewModel: SettingsViewModel = koinViewModel()
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    val isDarkMode = settingsState.isDarkMode
    val fontScale = settingsState.selectedFontSize.scale

    val colorScheme = if (isDarkMode) darkMiruColors() else lightMiruColors()

    // Apply font scale via density override
    val currentDensity = LocalDensity.current
    val scaledDensity = Density(
        density = currentDensity.density,
        fontScale = currentDensity.fontScale * fontScale
    )

    CompositionLocalProvider(LocalDensity provides scaledDensity) {
        MiruTheme(
            colorScheme = colorScheme,
            darkTheme = isDarkMode
        ) {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Only show bottom bar on main tabs (not on detail screen)
            val isOnMainTab = bottomNavTabs.any { tab ->
                currentDestination?.hierarchy?.any { it.route == tab.route } == true
            }

            Scaffold(
                bottomBar = {
                    if (isOnMainTab) {
                        NavigationBar {
                            bottomNavTabs.forEach { tab ->
                                val selected = currentDestination?.hierarchy?.any {
                                    it.route == tab.route
                                } == true

                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(tab.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.label
                                        )
                                    },
                                    label = { Text(tab.label) }
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = SampleRoutes.HOME,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(SampleRoutes.HOME) {
                        HomeScreen(
                            onNavigateToDetail = { id ->
                                navController.navigate(SampleRoutes.detail(id))
                            }
                        )
                    }

                    composable(SampleRoutes.SEARCH) {
                        SearchScreen(
                            onNavigateToDetail = { id ->
                                navController.navigate(SampleRoutes.detail(id))
                            }
                        )
                    }

                    composable(SampleRoutes.BOOKMARKS) {
                        BookmarkScreen(
                            onNavigateToDetail = { id ->
                                navController.navigate(SampleRoutes.detail(id))
                            }
                        )
                    }

                    composable(SampleRoutes.SETTINGS) {
                        SettingsScreen(viewModel = settingsViewModel)
                    }

                    composable(
                        route = SampleRoutes.DETAIL,
                        arguments = listOf(navArgument("articleId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val articleId = backStackEntry.getLongArgument("articleId")
                        val viewModel: DetailViewModel = koinViewModel { parametersOf(articleId) }
                        DetailScreen(
                            onNavigateBack = { navController.popBackStack() },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
