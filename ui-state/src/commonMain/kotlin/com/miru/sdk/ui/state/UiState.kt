package com.miru.sdk.ui.state

import com.miru.sdk.core.AppException
import com.miru.sdk.core.Resource

/**
 * Sealed interface representing the UI state for async operations.
 * Can be in one of four states: Idle, Loading, Success, or Error.
 */
sealed interface UiState<out T> {
    /** Idle state before any operation is triggered */
    data object Idle : UiState<Nothing>

    /** Loading state indicating an operation is in progress */
    data object Loading : UiState<Nothing>

    /** Success state containing the resulting data */
    data class Success<T>(val data: T) : UiState<T>

    /** Error state containing exception and optional message */
    data class Error(val exception: AppException, val message: String? = null) : UiState<Nothing>
}

/**
 * Checks if the current state is [UiState.Idle]
 */
fun <T> UiState<T>.isIdle(): Boolean = this is UiState.Idle

/**
 * Checks if the current state is [UiState.Loading]
 */
fun <T> UiState<T>.isLoading(): Boolean = this is UiState.Loading

/**
 * Checks if the current state is [UiState.Success]
 */
fun <T> UiState<T>.isSuccess(): Boolean = this is UiState.Success

/**
 * Checks if the current state is [UiState.Error]
 */
fun <T> UiState<T>.isError(): Boolean = this is UiState.Error

/**
 * Extracts data from [UiState.Success] or returns null
 */
fun <T> UiState<T>.getDataOrNull(): T? = (this as? UiState.Success)?.data

/**
 * Converts [Resource] to [UiState]
 *
 * Mapping:
 * - Resource.Loading -> UiState.Loading
 * - Resource.Success -> UiState.Success
 * - Resource.Error -> UiState.Error
 */
fun <T> Resource<T>.toUiState(): UiState<T> = when (this) {
    is Resource.Loading -> UiState.Loading
    is Resource.Success -> UiState.Success(data)
    is Resource.Error -> UiState.Error(exception, exception.message)
}
