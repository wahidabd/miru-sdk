package com.miru.sdk.auth

import com.miru.sdk.auth.apple.MiruAppleAuth
import com.miru.sdk.auth.facebook.MiruFacebookAuth
import com.miru.sdk.auth.google.MiruGoogleAuth
import org.koin.dsl.module

/**
 * Koin module for Auth dependencies.
 *
 * Usage:
 * ```
 * startKoin {
 *     modules(
 *         authModule,
 *         // ... other modules
 *     )
 * }
 *
 * // Don't forget to initialize Google Auth:
 * MiruGoogleAuth.initialize(serverClientId = "YOUR_SERVER_CLIENT_ID")
 * ```
 */
val authModule = module {
    single { MiruGoogleAuth() }
    single { MiruAppleAuth() }
    single { MiruFacebookAuth() }
    single { MiruAuthManager(get(), get(), get()) }
}
