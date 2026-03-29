package com.miru.sdk.persistent

import com.miru.sdk.persistent.preferences.MiruPreferences
import com.miru.sdk.persistent.preferences.createDataStore
import org.koin.dsl.module

/**
 * Koin module for Miru Persistent layer.
 *
 * Provides:
 * - [MiruPreferences] — DataStore-backed key-value storage
 *
 * Usage:
 * ```
 * // In your Koin setup
 * startKoin {
 *     modules(persistentModule, ...)
 * }
 *
 * // Inject
 * val prefs: MiruPreferences = get()
 * ```
 *
 * **Android setup**: Call [MiruPreferencesInitializer.init] and
 * [MiruDatabaseInitializer.init] in your Application.onCreate() before
 * starting Koin.
 */
val persistentModule = module {
    single { createDataStore() }
    single { MiruPreferences(get()) }
}
