package com.miru.sdk.di

import com.miru.sdk.core.logger.MiruLogger
import com.miru.sdk.di.modules.coreModule
import com.miru.sdk.di.modules.networkModule
import com.miru.sdk.di.modules.platformModule
import com.miru.sdk.network.config.NetworkConfig
import com.miru.sdk.network.token.TokenProvider
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * SDK initializer for configuring and starting the CMP SDK.
 *
 * Handles Koin DI setup, logger initialization, and module loading.
 */
object MiruSdkInitializer {
    /**
     * Initialize the CMP SDK with the provided configuration.
     *
     * @param config The SDK configuration containing network settings and optional modules
     */
    fun initialize(config: MiruSdkConfig) {
        // Initialize logger
        if (config.enableLogging) {
            MiruLogger.init()
            MiruLogger.d(tag = "MiruSdkInitializer", message = "Initializing CMP SDK")
        }

        // Build Koin modules
        val modules = mutableListOf<Module>()

        // Add core modules
        modules.add(coreModule())
        modules.add(networkModule(config.networkConfig, config.tokenProvider))
        modules.add(platformModule())

        // Add any additional modules provided by the user
        modules.addAll(config.additionalModules)

        // Start Koin
        startKoin {
            modules(modules)
        }

        if (config.enableLogging) {
            MiruLogger.d(tag = "MiruSdkInitializer", message = "CMP SDK initialized successfully")
        }
    }
}

/**
 * Configuration for the CMP SDK.
 *
 * @property networkConfig Configuration for the HTTP client
 * @property enableLogging Whether to enable SDK logging (default: true)
 * @property tokenProvider Optional token provider for authentication
 * @property additionalModules Additional Koin modules to include in the DI container
 */
data class MiruSdkConfig(
    val networkConfig: NetworkConfig,
    val enableLogging: Boolean = true,
    val tokenProvider: TokenProvider? = null,
    val additionalModules: List<Module> = emptyList()
)
