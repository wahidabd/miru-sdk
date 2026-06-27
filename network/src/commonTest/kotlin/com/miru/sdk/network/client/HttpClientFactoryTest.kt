package com.miru.sdk.network.client

import com.miru.sdk.network.config.NetworkConfig
import kotlin.test.Test
import kotlin.test.assertNotNull

class HttpClientFactoryTest {

    private val config = NetworkConfig(
        baseUrl = "https://api.example.com",
        enableLogging = false
    )

    @Test
    fun `create returns HttpClient when called with default empty interceptors`() {
        val client = HttpClientFactory.create(config)
        assertNotNull(client)
        client.close()
    }

    @Test
    fun `create returns HttpClient when called with explicit empty interceptors list`() {
        val client = HttpClientFactory.create(config, interceptors = emptyList())
        assertNotNull(client)
        client.close()
    }
}
