package com.miru.sdk.network.client

import io.ktor.client.engine.HttpClientEngine

expect fun createHttpEngine(interceptors: List<Any> = emptyList()): HttpClientEngine
