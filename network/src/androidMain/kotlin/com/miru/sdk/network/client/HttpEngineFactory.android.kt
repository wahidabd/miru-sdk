package com.miru.sdk.network.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.Interceptor

actual fun createHttpEngine(interceptors: List<Any>): HttpClientEngine {
    return OkHttp.create {
        config {
            interceptors.filterIsInstance<Interceptor>().forEach { interceptor ->
                addInterceptor(interceptor)
            }
        }
    }
}
