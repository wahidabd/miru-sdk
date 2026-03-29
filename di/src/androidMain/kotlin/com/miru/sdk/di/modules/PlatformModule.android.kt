package com.miru.sdk.di.modules

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android-specific Koin module providing platform-dependent implementations.
 *
 * Includes Android-specific services and configurations that are not needed
 * on other platforms.
 */
actual fun platformModule(): Module = module {
    // Platform-specific Android dependencies can be added here
    // Examples: SharedPreferences, DataStore, Android Context, etc.
}
