package com.miru.sdk.auth.google

import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import io.github.aakira.napier.Napier

/**
 * Miru Google Auth — standalone Google One Tap Sign-In (no Firebase).
 *
 * **Setup required:** Call [initialize] with your Google Server Client ID
 * before any sign-in attempt.
 *
 * Usage:
 * ```
 * // At app startup
 * MiruGoogleAuth.initialize(serverClientId = "YOUR_SERVER_CLIENT_ID")
 *
 * // Then use MiruGoogleSignInButton in your Composable
 * ```
 */
object MiruGoogleAuth {

    /**
     * Initialize Google Auth with server client ID.
     * Call once during app startup.
     *
     * @param serverClientId Your Google OAuth Server Client ID (from Google Cloud Console)
     */
    fun initialize(serverClientId: String) {
        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = serverClientId))
        Napier.d("MiruGoogleAuth initialized")
    }
}
