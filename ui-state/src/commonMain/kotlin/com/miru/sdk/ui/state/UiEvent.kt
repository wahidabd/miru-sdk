package com.miru.sdk.ui.state

/**
 * Sealed interface representing one-time UI events.
 * Events are sent and consumed once, ideal for navigation, notifications, and dialogs.
 */
sealed interface UiEvent {
    /**
     * Event to show a snackbar with a message and optional action
     */
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null
    ) : UiEvent

    /**
     * Event to navigate to a specific route
     */
    data class Navigate(val route: String) : UiEvent

    /**
     * Event to navigate back in the navigation stack
     */
    data object NavigateBack : UiEvent

    /**
     * Event to show a toast notification
     */
    data class ShowToast(val message: String) : UiEvent

    /**
     * Event to show a dialog with title and message
     */
    data class ShowDialog(val title: String, val message: String) : UiEvent
}
