package com.miru.sdk.network.config

/**
 * Configuration for the HTTP client.
 *
 * @property baseUrl The base URL for all API requests
 * @property connectTimeout Connection timeout in milliseconds (default: 30 seconds)
 * @property requestTimeout Total request timeout in milliseconds (default: 30 seconds)
 * @property socketTimeout Socket timeout in milliseconds (default: 30 seconds)
 * @property enableLogging Whether to enable HTTP request/response logging
 * @property logLevel The level of detail for HTTP logging
 */
data class NetworkConfig(
    val baseUrl: String,
    val connectTimeout: Long = 30_000L,
    val requestTimeout: Long = 30_000L,
    val socketTimeout: Long = 30_000L,
    val enableLogging: Boolean = true,
    val logLevel: LogLevel = LogLevel.BODY
)

/**
 * HTTP logging levels for Ktor client.
 */
enum class LogLevel {
    /** No logging */
    NONE,

    /** Log request/response headers only */
    HEADERS,

    /** Log headers and body content */
    BODY,

    /** Log all available information */
    ALL
}
