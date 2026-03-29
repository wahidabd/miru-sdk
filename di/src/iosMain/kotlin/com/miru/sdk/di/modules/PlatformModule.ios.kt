package com.miru.sdk.di.modules

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS-specific Koin module providing platform-dependent implementations.
 *
 * Includes iOS-specific services and configurations that are not needed
 * on other platforms.
 */
actual fun platformModule(): Module = module {
    // Platform-specific iOS dependencies can be added here
    // Examples: UserDefaults, Keychain helpers, iOS-specific APIs, etc.
}
