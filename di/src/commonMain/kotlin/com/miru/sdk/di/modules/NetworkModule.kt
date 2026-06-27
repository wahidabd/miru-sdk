package com.miru.sdk.di.modules

import com.miru.sdk.network.client.HttpClientFactory
import com.miru.sdk.network.config.NetworkConfig
import com.miru.sdk.network.token.TokenProvider
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun networkModule(
    networkConfig: NetworkConfig,
    tokenProvider: TokenProvider?
): Module = module {
    single<NetworkConfig> { networkConfig }

    tokenProvider?.let { provider ->
        single<TokenProvider> { provider }
    }

    single<HttpClient> {
        val interceptors = getOrNull<List<Any>>(named("networkInterceptors")) ?: emptyList()
        HttpClientFactory.create(
            config = networkConfig,
            tokenProvider = tokenProvider,
            interceptors = interceptors
        )
    }
}
