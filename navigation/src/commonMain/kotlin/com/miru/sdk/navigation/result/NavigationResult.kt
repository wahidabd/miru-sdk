package com.miru.sdk.navigation.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry

/**
 * Set a result that can be observed by the previous screen in the navigation stack.
 * Results are stored in the SavedStateHandle and can be retrieved using [observeResult].
 *
 * @param T The type of the result value
 * @param key The key under which to store the result
 * @param value The result value to store
 */
fun <T> NavBackStackEntry.setResult(
    key: String,
    value: T?,
) {
    savedStateHandle[key] = value
}

/**
 * Observe a result from a child screen in the navigation stack.
 * Returns a State that will emit the result value whenever it changes.
 *
 * @param T The type of the result value
 * @param key The key under which the result is stored
 * @param initialValue The initial value before any result is received
 * @return A State containing the result value, or null if not set
 */
@Composable
fun <T> NavBackStackEntry.observeResult(
    key: String,
    initialValue: T? = null,
): State<T?> {
    return savedStateHandle.getStateFlow<T?>(key, initialValue).collectAsState()
}

/**
 * Clear a result after it has been consumed.
 *
 * @param key The key of the result to clear
 */
fun NavBackStackEntry.clearResult(key: String) {
    savedStateHandle[key] = null
}

/**
 * Helper class for managing navigation results in a type-safe manner.
 *
 * Example usage:
 * ```
 * // In the source screen (where you want to receive a result):
 * val result = navBackStackEntry.observeResult<String>("myKey")
 *
 * // In the destination screen (where you generate the result):
 * navBackStackEntry.setResult("myKey", "result_value")
 * navController.popBackStack()
 * ```
 */
object NavigationResultHelper {
    /**
     * Create a result key with a unique identifier.
     * This helps avoid key collisions when passing multiple results.
     *
     * @param screenName The name of the screen
     * @param resultType The type of result being passed
     * @return A unique result key
     */
    fun createResultKey(
        screenName: String,
        resultType: String,
    ): String {
        return "${screenName}_${resultType}_result"
    }

    /**
     * Standard result keys for common scenarios.
     */
    object StandardKeys {
        const val RESULT_OK = "result_ok"
        const val RESULT_CANCELLED = "result_cancelled"
        const val RESULT_ERROR = "result_error"
        const val RESULT_DATA = "result_data"
    }
}
