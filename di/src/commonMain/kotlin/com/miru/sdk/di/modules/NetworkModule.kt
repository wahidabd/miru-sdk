package com.miru.sdk.di.modules

import com.miru.sdk.network.client.HttpClientFactory
import com.miru.sdk.network.config.NetworkConfig
import com.miru.sdk.network.token.TokenProvider
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module providing network-related dependencies.
 *
 * Configures and provides the Ktor [HttpClient] using [HttpClientFactory]
 * with authentication, logging, and serialization support.
 *
 * @param networkConfig The network configuration settings
 * @param tokenProvider Optional token provider for authentication
 * @return A Koin [Module] with network dependencies
 */
internal fun networkModule(
    networkConfig: NetworkConfig,
    tokenProvider: TokenProvider?
): Module = module {
    // Provide the network configuration as a singleton
    single<NetworkConfig> { networkConfig }

    // Provide the token provider if configured
    tokenProvider?.let { provider ->
        single<TokenProvider> { provider }
    }

    // Provide the HttpClient using HttpClientFactory
    single<HttpClient> {
        HttpClientFactory.create(
            config = networkConfig,
            tokenProvider = tokenProvider
        )
    }
}
