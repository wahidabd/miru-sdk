package com.miru.sdk.network.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun createHttpEngine(interceptors: List<Any>): HttpClientEngine {
    return Darwin.create()
}
