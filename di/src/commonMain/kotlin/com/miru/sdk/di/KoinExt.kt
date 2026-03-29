package com.miru.sdk.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.scope.Scope

/**
 * Convenient extension functions for Koin dependency injection.
 *
 * Provides helpers for retrieving dependencies in various contexts.
 */

/**
 * Get a dependency from Koin using inline reified type parameter.
 *
 * Example:
 * ```
 * val myService: MyService = koinGet()
 * ```
 *
 * @return The resolved instance from Koin container
 */
inline fun <reified T> koinGet(): T = org.koin.mp.KoinPlatform.getKoin().get()

/**
 * Get a dependency from a specific Koin scope.
 *
 * @param scope The Koin scope to retrieve from
 * @return The resolved instance from the specified scope
 */
inline fun <reified T> Scope.koinGet(): T = get()

/**
 * Composable function to inject dependencies into Compose functions.
 *
 * This is a convenience wrapper around Koin's koinInject that uses the
 * reified type parameter for cleaner syntax in Compose.
 *
 * Example:
 * ```
 * @Composable
 * fun MyScreen() {
 *     val viewModel: MyViewModel = koinInject()
 *     // ...
 * }
 * ```
 *
 * @return The resolved instance from Koin container
 */
@Composable
inline fun <reified T> koinInject(): T = koinInject()

/**
 * Composable function to inject ViewModel instances in Compose.
 *
 * This is a convenience wrapper around Koin's koinViewModel that uses the
 * reified type parameter.
 *
 * Example:
 * ```
 * @Composable
 * fun MyScreen() {
 *     val viewModel: MyViewModel = koinViewModel()
 *     // ...
 * }
 * ```
 *
 * @return The resolved ViewModel instance from Koin container
 */
@Composable
inline fun <reified T : ViewModel> koinViewModel(): T = koinViewModel()

/**
 * Get a lazy dependency from Koin that is only resolved on first access.
 *
 * Example:
 * ```
 * val myService: MyService by koinLazy()
 * ```
 *
 * @return A lazy delegate that resolves the dependency when accessed
 */
inline fun <reified T> koinLazy(): Lazy<T> = lazy {
    koinGet<T>()
}

/**
 * Check if a dependency is registered in Koin for the given type.
 *
 * @return true if the dependency is registered, false otherwise
 */
inline fun <reified T> koinHasDefinition(): Boolean {
    return try {
        koinGet<T>()
        true
    } catch (e: Exception) {
        false
    }
}
