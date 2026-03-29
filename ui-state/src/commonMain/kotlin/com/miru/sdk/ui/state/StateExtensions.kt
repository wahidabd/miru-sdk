package com.miru.sdk.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miru.sdk.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

/**
 * Converts a Flow of [Resource] to a Flow of [UiState].
 *
 * @return A Flow that emits [UiState] values transformed from [Resource]
 */
fun <T> Flow<Resource<T>>.asUiState(): Flow<UiState<T>> = map { it.toUiState() }

/**
 * Collects a [StateFlow] as a Compose [State] with lifecycle awareness.
 *
 * This is a convenience wrapper that ensures the flow is only collected
 * when the lifecycle is at least in the STARTED state.
 *
 * @return A Compose [State] containing the current value of the [StateFlow]
 */
@Composable
fun <T> StateFlow<T>.collectAsStateLifecycleAware(): State<T> {
    return this.collectAsStateWithLifecycle()
}
