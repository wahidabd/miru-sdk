package com.miru.sdk.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

/**
 * A composable that sets up the navigation host for the Miru SDK.
 * Automatically collects navigation commands from the NavigationManager and dispatches them to the NavController.
 *
 * @param navigationManager The NavigationManager instance to collect navigation commands from
 * @param navController The NavHostController instance to dispatch navigation events to
 * @param startDestination The starting route for the navigation graph
 * @param modifier Optional modifier to apply to the NavHost
 * @param builder Lambda to build the navigation graph
 */
@Composable
fun MiruNavHost(
    navigationManager: NavigationManager,
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    builder: NavGraphBuilder.() -> Unit,
) {
    // Collect navigation commands and dispatch them to the NavController
    LaunchedEffect(navigationManager, navController) {
        navigationManager.navigationCommands.collect { command ->
            when (command) {
                is NavigationCommand.NavigateTo -> {
                    navController.navigateSafely(
                        route = command.route,
                        navOptions = {
                            command.popUpTo?.let { route ->
                                popUpTo(route) {
                                    inclusive = command.inclusive
                                }
                            }
                            launchSingleTop = command.singleTop
                        }
                    )
                }

                NavigationCommand.NavigateBack -> {
                    navController.popBackStack()
                }

                is NavigationCommand.PopUpTo -> {
                    navController.popBackStack(
                        route = command.route,
                        inclusive = command.inclusive,
                    )
                }

                is NavigationCommand.NavigateAndClearStack -> {
                    navController.navigateSafely(
                        route = command.route,
                        navOptions = {
                            popUpTo(startDestination) {
                                inclusive = true
                            }
                        }
                    )
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        builder = builder,
    )
}

/**
 * Creates and provides a NavController and NavigationManager for use in composables.
 * Useful as a convenience composable to set up both components together.
 *
 * @param startDestination The starting route for the navigation graph
 * @param modifier Optional modifier to apply to the NavHost
 * @param navigationManager Optional pre-configured NavigationManager instance (defaults to NavigationManagerImpl)
 * @param builder Lambda to build the navigation graph
 */
@Composable
fun MiruNavigationHost(
    startDestination: String,
    modifier: Modifier = Modifier,
    navigationManager: NavigationManager = remember { NavigationManagerImpl() },
    builder: NavGraphBuilder.() -> Unit,
) {
    val navController = rememberNavController()

    MiruNavHost(
        navigationManager = navigationManager,
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        builder = builder,
    )
}
