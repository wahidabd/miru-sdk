package com.miru.sdk.network.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

/**
 * Android-specific HTTP client engine implementation using OkHttp.
 */
actual fun createHttpEngine(): HttpClientEngine {
    return OkHttp.create()
}
