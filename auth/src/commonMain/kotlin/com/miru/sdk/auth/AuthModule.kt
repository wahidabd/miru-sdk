package com.miru.sdk.auth

import com.miru.sdk.auth.apple.MiruAppleAuth
import com.miru.sdk.auth.facebook.MiruFacebookAuth
import org.koin.dsl.module

/**
 * Koin module for Auth dependencies.
 *
 * Usage:
 * ```
 * startKoin {
 *     modules(authModule)
 * }
 *
 * // Initialize Google Auth:
 * MiruGoogleAuth.initialize(serverClientId = "YOUR_SERVER_CLIENT_ID")
 * ```
 */
val authModule = module {
    single { MiruAuthManager() }
    single { MiruAppleAuth() }
    single { MiruFacebookAuth() }
}
