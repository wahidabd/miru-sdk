package com.miru.sdk.sample

import android.app.Application
import com.miru.sdk.di.MiruSdkConfig
import com.miru.sdk.di.MiruSdkInitializer
import com.miru.sdk.network.config.NetworkConfig
import com.miru.sdk.persistent.database.MiruDatabaseInitializer
import com.miru.sdk.persistent.persistentModule
import com.miru.sdk.persistent.preferences.MiruPreferencesInitializer
import com.miru.sdk.sample.app.BuildConfig
import com.miru.sdk.sample.di.SampleDatabaseInitializer
import com.miru.sdk.sample.di.createSampleModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

/**
 * Sample application entry point.
 * Initializes the Miru SDK and all platform-specific dependencies.
 */
class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize logging
        Napier.base(DebugAntilog())

        // Initialize platform-specific components
        MiruPreferencesInitializer.init(this)
        MiruDatabaseInitializer.init(this)
        SampleDatabaseInitializer.init(this)

        // Read API key from BuildConfig (sourced from env.properties)
        val newsApiKey = BuildConfig.NEWS_API_KEY

        // Initialize Miru SDK with NewsAPI.org base URL
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
}
