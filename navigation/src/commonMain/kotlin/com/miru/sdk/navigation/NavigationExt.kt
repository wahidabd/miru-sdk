package com.miru.sdk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions

/**
 * Safely navigate to a destination, preventing duplicate navigation attempts.
 * If a navigation to the same route is already in progress or completed, this will be ignored.
 *
 * @param route The destination route
 * @param navOptions Optional NavOptions to customize the navigation behavior
 */
fun NavController.navigateSafely(
    route: String,
    navOptions: MiruNavOptionsBuilder.() -> Unit = {},
) {
    val builder = MiruNavOptionsBuilder().apply(navOptions)
    val options = builder.build()
    navigate(route, options)
}

/**
 * Get the current route as a string.
 * Returns null if no route is currently set.
 *
 * @return The current route string, or null if not available
 */
fun NavController.currentRoute(): String? {
    return currentBackStackEntry?.destination?.route
}

/**
 * Get a typed argument from the NavBackStackEntry.
 * Provides type-safe retrieval of navigation arguments.
 *
 * @param T The type of the argument
 * @param key The argument key
 * @return The argument value, or null if not found or of incorrect type
 */
@Composable
inline fun <reified T> NavBackStackEntry.getArgument(key: String): T? {
    return arguments?.getString(key) as? T
}

/**
 * Get a string argument from the NavBackStackEntry.
 *
 * @param key The argument key
 * @return The string argument value, or null if not found
 */
fun NavBackStackEntry.getStringArgument(key: String): String? {
    return arguments?.getString(key)
}

/**
 * Get an int argument from the NavBackStackEntry.
 *
 * @param key The argument key
 * @return The int argument value, or 0 if not found
 */
fun NavBackStackEntry.getIntArgument(key: String): Int {
    return arguments?.getInt(key) ?: 0
}

/**
 * Get a long argument from the NavBackStackEntry.
 *
 * @param key The argument key
 * @return The long argument value, or 0L if not found
 */
fun NavBackStackEntry.getLongArgument(key: String): Long {
    return arguments?.getLong(key) ?: 0L
}

/**
 * Get a boolean argument from the NavBackStackEntry.
 *
 * @param key The argument key
 * @return The boolean argument value, or false if not found
 */
fun NavBackStackEntry.getBooleanArgument(key: String): Boolean {
    return arguments?.getBoolean(key) ?: false
}

/**
 * Builder DSL for creating NavOptions with a fluent API.
 * Provides helper functions for common navigation scenarios.
 */
class MiruNavOptionsBuilder {
    var launchSingleTop: Boolean = false
    var popUpTo: PopUpToBuilder? = null

    /**
     * Configure pop-up behavior.
     *
     * @param route The route to pop up to
     * @param builder Lambda to configure pop-up options
     */
    fun popUpTo(
        route: String,
        builder: PopUpToBuilder.() -> Unit = {},
    ) {
        popUpTo = PopUpToBuilder(route).apply(builder)
    }

    /**
     * Build the NavOptions object.
     *
     * @return The configured NavOptions
     */
    fun build(): NavOptions {
        return NavOptions.Builder().apply {
            if (launchSingleTop) {
                setLaunchSingleTop(true)
            }
            popUpTo?.let {
                setPopUpTo(it.route, it.inclusive)
            }
        }.build()
    }

    /**
     * Pop-up configuration builder.
     *
     * @property route The route to pop up to
     * @property inclusive Whether to include the route in the pop operation
     */
    class PopUpToBuilder(
        val route: String,
        var inclusive: Boolean = false,
    )
}

/**
 * Extension function for NavController to navigate with DSL-style NavOptions.
 *
 * @param route The destination route
 * @param navOptions Lambda to configure NavOptions
 */
fun NavController.navigate(
    route: String,
    navOptions: MiruNavOptionsBuilder.() -> Unit,
) {
    val builder = MiruNavOptionsBuilder().apply(navOptions)
    navigate(route, builder.build())
}
