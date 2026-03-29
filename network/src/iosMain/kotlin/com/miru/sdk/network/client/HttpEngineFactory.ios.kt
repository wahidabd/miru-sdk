package com.miru.sdk.network.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

/**
 * iOS-specific HTTP client engine implementation using Darwin (native iOS HTTP).
 */
actual fun createHttpEngine(): HttpClientEngine {
    return Darwin.create()
}
