package com.miru.sdk.di

import android.content.Context
import com.miru.sdk.core.logger.MiruLogger
import com.miru.sdk.di.modules.coreModule
import com.miru.sdk.di.modules.networkModule
import com.miru.sdk.di.modules.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun MiruSdkInitializer.initialize(context: Context, config: MiruSdkConfig) {
    if (config.enableLogging) {
        MiruLogger.init()
        MiruLogger.d(tag = "MiruSdkInitializer", message = "Initializing Miru SDK")
    }

    val modules = mutableListOf<Module>()
    modules.add(coreModule())
    modules.add(networkModule(config.networkConfig, config.tokenProvider))
    modules.add(platformModule())
    modules.addAll(config.additionalModules)

    startKoin {
        androidContext(context)
        modules(modules)
    }

    if (config.enableLogging) {
        MiruLogger.d(tag = "MiruSdkInitializer", message = "Miru SDK initialized successfully")
    }
}
