package com.miru.sdk.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * A mutable event flow backed by a Channel for sending and collecting one-time events.
 *
 * @param T The type of events being emitted
 */
class MutableEventFlow<T> {
    private val channel = Channel<T>(Channel.BUFFERED)

    /**
     * Returns a Flow representation of events
     */
    fun asFlow(): Flow<T> = channel.receiveAsFlow()

    /**
     * Sends an event to the channel
     *
     * @param event The event to send
     */
    suspend fun send(event: T) {
        channel.send(event)
    }

    /**
     * Tries to send an event without suspending
     *
     * @param event The event to send
     * @return true if the event was sent successfully, false otherwise
     */
    fun trySend(event: T): Boolean = channel.trySend(event).isSuccess
}

/**
 * Collects events from a [MutableEventFlow] and invokes the handler in a LaunchedEffect.
 * This is a composable helper to collect events with lifecycle awareness.
 *
 * @param handler Callback invoked when an event is emitted
 */
@Composable
fun <T> MutableEventFlow<T>.collectAsEffect(handler: (T) -> Unit) {
    LaunchedEffect(this) {
        asFlow().collect { event ->
            handler(event)
        }
    }
}
