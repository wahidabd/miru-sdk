package com.miru.sdk.auth.google

import com.miru.sdk.auth.AuthException
import com.miru.sdk.auth.AuthProvider
import com.miru.sdk.auth.AuthResult
import com.miru.sdk.core.Resource
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Miru Google Auth — wraps KMPAuth's Google One Tap Sign-In for KMP.
 *
 * **Setup required:** Call [initialize] with your Google Server Client ID before use.
 *
 * Usage:
 * ```
 * // 1. Initialize (once, at app startup)
 * MiruGoogleAuth.initialize(serverClientId = "YOUR_SERVER_CLIENT_ID")
 *
 * // 2. Use in Composable — see MiruGoogleSignInButton
 * ```
 */
class MiruGoogleAuth {

    companion object {
        /**
         * Initialize Google Auth with server client ID.
         * Call this once during app startup, before any sign-in attempts.
         *
         * @param serverClientId Your Google OAuth Server Client ID (from Google Cloud Console)
         */
        fun initialize(serverClientId: String) {
            GoogleAuthCredentials.create(serverClientId = serverClientId)
            Napier.d("MiruGoogleAuth initialized with serverClientId")
        }
    }

    /**
     * Handle the Google Sign-In result from [GoogleAuthProvider].
     * Converts KMPAuth's GoogleUser to our [AuthResult] wrapped in [Resource].
     */
    fun handleResult(
        idToken: String?,
        displayName: String? = null,
        email: String? = null,
        photoUrl: String? = null,
    ): Resource<AuthResult> {
        return if (idToken != null) {
            val result = AuthResult(
                idToken = idToken,
                displayName = displayName,
                email = email,
                photoUrl = photoUrl,
                provider = AuthProvider.GOOGLE,
            )
            Napier.d("Google sign-in success: ${email ?: "no email"}")
            Resource.Success(result)
        } else {
            Napier.e("Google sign-in failed: no idToken")
            Resource.Error(AuthException.NoToken("Google sign-in returned no ID token"))
        }
    }

    /**
     * Handle Google Sign-In error.
     */
    fun handleError(error: Throwable): Resource<AuthResult> {
        Napier.e("Google sign-in error", error)
        return Resource.Error(
            AuthException.ProviderError(
                provider = AuthProvider.GOOGLE,
                message = error.message ?: "Google sign-in failed",
                cause = error,
            )
        )
    }
}
