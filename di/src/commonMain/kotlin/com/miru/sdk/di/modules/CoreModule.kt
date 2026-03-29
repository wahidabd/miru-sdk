package com.miru.sdk.di.modules

import com.miru.sdk.core.dispatcher.DispatcherProvider
import com.miru.sdk.core.dispatcher.provideDispatcher
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin module providing core SDK dependencies.
 *
 * Includes dispatcher management and other core-level singletons.
 */
internal fun coreModule(): Module = module {
    // Provide the platform-specific DispatcherProvider
    single<DispatcherProvider> {
        provideDispatcher()
    }
}
