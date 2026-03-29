package com.miru.sdk.network.client

import io.ktor.client.engine.HttpClientEngine

/**
 * Factory for creating platform-specific HTTP client engines.
 * Platform implementations must be provided for Android and iOS.
 */
expect fun createHttpEngine(): HttpClientEngine
