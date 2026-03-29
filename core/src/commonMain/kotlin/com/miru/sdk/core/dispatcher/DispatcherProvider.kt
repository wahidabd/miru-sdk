package com.miru.sdk.core.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Interface providing CoroutineDispatchers for different purposes.
 */
interface DispatcherProvider {
    /**
     * The main/UI dispatcher.
     */
    val main: CoroutineDispatcher

    /**
     * The IO dispatcher for I/O operations.
     */
    val io: CoroutineDispatcher

    /**
     * The default dispatcher for CPU-intensive work.
     */
    val default: CoroutineDispatcher

    /**
     * The unconfined dispatcher.
     */
    val unconfined: CoroutineDispatcher
}

/**
 * Provides a platform-specific DispatcherProvider implementation.
 */
expect fun provideDispatcher(): DispatcherProvider
