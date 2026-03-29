package com.miru.sdk.auth.facebook

import com.miru.sdk.auth.AuthException
import com.miru.sdk.auth.AuthProvider
import com.miru.sdk.auth.AuthResult
import com.miru.sdk.core.Resource
import io.github.aakira.napier.Napier

/**
 * Miru Facebook Auth — wraps KMPAuth's Facebook Login via Firebase for KMP.
 *
 * Facebook Login uses Firebase Authentication under the hood.
 * Make sure Firebase is configured in your project.
 *
 * Usage:
 * ```
 * // Use in Composable — see MiruFacebookSignInButton
 * ```
 */
class MiruFacebookAuth {

    /**
     * Handle the Facebook Sign-In result from Firebase.
     * Converts Firebase result to our [AuthResult] wrapped in [Resource].
     */
    fun handleResult(
        accessToken: String?,
        displayName: String? = null,
        email: String? = null,
        photoUrl: String? = null,
    ): Resource<AuthResult> {
        return if (accessToken != null) {
            val result = AuthResult(
                accessToken = accessToken,
                displayName = displayName,
                email = email,
                photoUrl = photoUrl,
                provider = AuthProvider.FACEBOOK,
            )
            Napier.d("Facebook sign-in success: ${email ?: "no email"}")
            Resource.Success(result)
        } else {
            Napier.e("Facebook sign-in failed: no accessToken")
            Resource.Error(AuthException.NoToken("Facebook sign-in returned no access token"))
        }
    }

    /**
     * Handle Facebook Sign-In error.
     */
    fun handleError(error: Throwable): Resource<AuthResult> {
        Napier.e("Facebook sign-in error", error)
        return Resource.Error(
            AuthException.ProviderError(
                provider = AuthProvider.FACEBOOK,
                message = error.message ?: "Facebook sign-in failed",
                cause = error,
            )
        )
    }
}
