package com.miru.sdk.sample

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miru.sdk.sample.presentation.SampleRoutes
import com.miru.sdk.sample.presentation.bookmark.BookmarkScreen
import com.miru.sdk.sample.presentation.detail.DetailScreen
import com.miru.sdk.sample.presentation.detail.DetailViewModel
import com.miru.sdk.sample.presentation.home.HomeScreen
import com.miru.sdk.sample.presentation.search.SearchScreen
import com.miru.sdk.sample.presentation.settings.SettingsScreen
import com.miru.sdk.ui.components.theme.MiruTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Main entry point for the Miru News sample app.
 *
 * Demonstrates clean architecture with the Miru SDK:
 * - Home: Article list with category filters (network + domain use cases)
 * - Search: Full-text article search (network)
 * - Detail: Article detail with bookmark toggle (network + persistent)
 * - Bookmarks: Saved articles list (Room database)
 * - Settings: App preferences (DataStore)
 */
@Composable
fun SampleApp() {
    MiruTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SampleRoutes.HOME
        ) {
            composable(SampleRoutes.HOME) {
                HomeScreen(
                    onNavigateToDetail = { id ->
                        navController.navigate(SampleRoutes.detail(id))
                    },
                    onNavigateToSearch = {
                        navController.navigate(SampleRoutes.SEARCH)
                    },
                    onNavigateToBookmarks = {
                        navController.navigate(SampleRoutes.BOOKMARKS)
                    },
                    onNavigateToSettings = {
                        navController.navigate(SampleRoutes.SETTINGS)
                    }
                )
            }

            composable(SampleRoutes.SEARCH) {
                SearchScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { id ->
                        navController.navigate(SampleRoutes.detail(id))
                    }
                )
            }

            composable(SampleRoutes.BOOKMARKS) {
                BookmarkScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { id ->
                        navController.navigate(SampleRoutes.detail(id))
                    }
                )
            }

            composable(SampleRoutes.SETTINGS) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = SampleRoutes.DETAIL,
                arguments = listOf(navArgument("articleId") { type = NavType.LongType })
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getLong("articleId") ?: 0L
                val viewModel: DetailViewModel = koinViewModel { parametersOf(articleId) }
                DetailScreen(
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }
    }
}
