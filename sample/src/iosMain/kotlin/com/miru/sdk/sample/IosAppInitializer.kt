package com.miru.sdk.sample

import com.miru.sdk.di.MiruSdkConfig
import com.miru.sdk.di.MiruSdkInitializer
import com.miru.sdk.network.config.NetworkConfig
import com.miru.sdk.persistent.persistentModule
import com.miru.sdk.sample.di.createSampleModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

/**
 * Initializes the Miru SDK for iOS.
 * Call this once from the Swift AppDelegate / App.init before showing any UI.
 *
 * Usage from Swift:
 * ```swift
 * IosAppInitializerKt.initializeSdk(newsApiKey: "YOUR_KEY")
 * ```
 */
fun initializeSdk(newsApiKey: String) {
    Napier.base(DebugAntilog())

    MiruSdkInitializer.initialize(
        MiruSdkConfig(
            networkConfig = NetworkConfig(
                baseUrl = "https://newsapi.org/v2/",
                enableLogging = true
            ),
            enableLogging = true,
            additionalModules = listOf(
                persistentModule,
                createSampleModule(newsApiKey)
            )
        )
    )
}
