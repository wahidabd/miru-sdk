package com.miru.sdk.auth.apple

import com.miru.sdk.auth.AuthException
import com.miru.sdk.auth.AuthProvider
import com.miru.sdk.auth.AuthResult
import com.miru.sdk.core.Resource
import io.github.aakira.napier.Napier

/**
 * Miru Apple Auth — wraps KMPAuth's Apple Sign-In via Firebase for KMP.
 *
 * Apple Sign-In uses Firebase Authentication under the hood.
 * Make sure Firebase is configured in your project.
 *
 * Usage:
 * ```
 * // Use in Composable — see MiruAppleSignInButton
 * ```
 */
class MiruAppleAuth {

    /**
     * Handle the Apple Sign-In result from Firebase.
     * Converts Firebase result to our [AuthResult] wrapped in [Resource].
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
                provider = AuthProvider.APPLE,
            )
            Napier.d("Apple sign-in success: ${email ?: "no email"}")
            Resource.Success(result)
        } else {
            Napier.e("Apple sign-in failed: no idToken")
            Resource.Error(AuthException.NoToken("Apple sign-in returned no ID token"))
        }
    }

    /**
     * Handle Apple Sign-In error.
     */
    fun handleError(error: Throwable): Resource<AuthResult> {
        Napier.e("Apple sign-in error", error)
        return Resource.Error(
            AuthException.ProviderError(
                provider = AuthProvider.APPLE,
                message = error.message ?: "Apple sign-in failed",
                cause = error,
            )
        )
    }
}
