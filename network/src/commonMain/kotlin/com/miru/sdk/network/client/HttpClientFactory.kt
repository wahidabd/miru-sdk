package com.miru.sdk.network.client

import com.miru.sdk.network.config.LogLevel
import com.miru.sdk.network.config.NetworkConfig
import com.miru.sdk.network.token.TokenProvider
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel as KtorLogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Factory for creating and configuring Ktor HTTP clients.
 */
object HttpClientFactory {
    /**
     * Create an HttpClient with the given configuration.
     *
     * @param config Network configuration settings
     * @param tokenProvider Optional token provider for handling authentication
     * @return Configured HttpClient instance
     */
    fun create(config: NetworkConfig, tokenProvider: TokenProvider? = null): HttpClient {
        return HttpClient(createHttpEngine()) {
            // Configure timeouts
            install(HttpTimeout) {
                connectTimeoutMillis = config.connectTimeout
                requestTimeoutMillis = config.requestTimeout
                socketTimeoutMillis = config.socketTimeout
            }

            // Configure JSON serialization
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                    }
                )
            }

            // Configure logging
            if (config.enableLogging) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Napier.d(tag = "HttpClient", message = message)
                        }
                    }
                    level = mapLogLevel(config.logLevel)
                }
            }

            // Configure default requests
            install(DefaultRequest) {
                url(config.baseUrl)
                contentType(ContentType.Application.Json)
                header("Accept", ContentType.Application.Json)
            }
        }
    }

    /**
     * Map network config log level to Ktor log level.
     */
    private fun mapLogLevel(logLevel: LogLevel): KtorLogLevel {
        return when (logLevel) {
            LogLevel.NONE -> KtorLogLevel.NONE
            LogLevel.HEADERS -> KtorLogLevel.HEADERS
            LogLevel.BODY -> KtorLogLevel.BODY
            LogLevel.ALL -> KtorLogLevel.ALL
        }
    }
}
