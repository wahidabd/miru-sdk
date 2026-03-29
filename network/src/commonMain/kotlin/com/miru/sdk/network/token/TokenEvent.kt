package com.miru.sdk.network.token

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Events related to token lifecycle.
 */
sealed class TokenEvent {
    /** Emitted when the access token has expired */
    object TokenExpired : TokenEvent()

    /** Emitted when the token has been refreshed successfully */
    object TokenRefreshed : TokenEvent()

    /** Emitted when the user should be logged out forcefully */
    object ForceLogout : TokenEvent()
}

/**
 * Central event bus for token lifecycle events.
 * Components can collect from [events] to react to token changes.
 */
object TokenEventBus {
    private val _events = MutableSharedFlow<TokenEvent>(replay = 0, extraBufferCapacity = 1)

    /** Public flow of token events */
    val events: SharedFlow<TokenEvent> = _events.asSharedFlow()

    /**
     * Emit a token event.
     *
     * @param event The event to emit
     */
    suspend fun emit(event: TokenEvent) {
        _events.emit(event)
    }
}
