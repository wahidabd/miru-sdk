package com.miru.sdk.di.modules

import org.koin.core.module.Module

/**
 * Expect declaration for platform-specific Koin modules.
 *
 * Each platform (Android, iOS) provides its own implementation with
 * platform-specific dependencies and configurations.
 */
expect fun platformModule(): Module
