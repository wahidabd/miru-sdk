package com.miru.sdk.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miru.sdk.core.AppException
import com.miru.sdk.core.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class providing state and event management for UI layers.
 *
 * Subclasses should define their own State and Event types and implement business logic
 * using [setState], [sendEvent], and [launch] helper functions.
 *
 * @param State The type of UI state managed by this ViewModel
 * @param Event The type of UI events emitted by this ViewModel
 * @param initialState The initial state when the ViewModel is created
 */
abstract class BaseViewModel<State, Event>(initialState: State) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)

    /**
     * The current UI state as a StateFlow
     */
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _events = MutableEventFlow<Event>()

    /**
     * Events emitted by this ViewModel
     */
    val events: MutableEventFlow<Event> = _events

    /**
     * The current state value
     */
    protected val currentState: State
        get() = _uiState.value

    /**
     * Updates the state using a reducer function
     *
     * @param reduce A lambda that receives the current state and returns the new state
     */
    protected fun setState(reduce: State.() -> State) {
        _uiState.value = _uiState.value.reduce()
    }

    /**
     * Sends an event to be collected by the UI
     *
     * @param event The event to send
     */
    protected fun sendEvent(event: Event) {
        _events.trySend(event)
    }

    /**
     * Launches a coroutine in the ViewModel's scope
     *
     * @param block The suspend block to execute
     * @return The launched Job
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(block = block)
    }

    /**
     * Collects a Flow of Resources and updates state accordingly.
     * Handles loading, success, and error states automatically.
     *
     * @param T The type of data in the Resource
     * @param onLoading Optional callback when loading starts
     * @param onSuccess Callback when data is successfully loaded
     * @param onError Optional callback when an error occurs
     * @return The launched Job
     */
    protected fun <T> Flow<Resource<T>>.collectResource(
        onLoading: (suspend () -> Unit)? = null,
        onSuccess: suspend (T) -> Unit,
        onError: (suspend (AppException) -> Unit)? = null
    ): Job = launch {
        collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    onLoading?.invoke()
                }
                is Resource.Success -> {
                    onSuccess(resource.data)
                }
                is Resource.Error -> {
                    onError?.invoke(resource.exception)
                }
            }
        }
    }
}
