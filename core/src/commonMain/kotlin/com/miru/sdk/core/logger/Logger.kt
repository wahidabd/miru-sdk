package com.miru.sdk.core.logger

import io.github.aakira.napier.Napier

/**
 * Multiplatform logger wrapper using Napier.
 *
 * Provides simple logging methods for different severity levels.
 */
object MiruLogger {
    private const val DEFAULT_TAG = "MiruSDK"

    /**
     * Initializes the logger.
     *
     * Should be called once during app startup.
     */
    fun init() {
        // Napier is auto-initialized with sensible defaults
        // This method is a placeholder for future configuration
    }

    /**
     * Logs a debug message.
     *
     * @param tag the log tag
     * @param message the message to log
     */
    fun d(tag: String = DEFAULT_TAG, message: String) {
        Napier.d(message = message, tag = tag)
    }

    /**
     * Logs an info message.
     *
     * @param tag the log tag
     * @param message the message to log
     */
    fun i(tag: String = DEFAULT_TAG, message: String) {
        Napier.i(message = message, tag = tag)
    }

    /**
     * Logs a warning message.
     *
     * @param tag the log tag
     * @param message the message to log
     */
    fun w(tag: String = DEFAULT_TAG, message: String) {
        Napier.w(message = message, tag = tag)
    }

    /**
     * Logs an error message.
     *
     * @param tag the log tag
     * @param message the message to log
     * @param throwable optional exception to log
     */
    fun e(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        Napier.e(message = message, tag = tag, throwable = throwable)
    }
}
