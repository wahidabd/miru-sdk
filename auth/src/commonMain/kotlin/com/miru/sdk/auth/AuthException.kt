package com.miru.sdk.auth

import com.miru.sdk.core.AppException

/**
 * Auth-specific exceptions extending [AppException].
 */
sealed class AuthException(message: String, cause: Throwable? = null) : AppException(message, cause) {

    /** User cancelled the sign-in flow. */
    class Cancelled(
        message: String = "Sign-in was cancelled by user"
    ) : AuthException(message)

    /** The OAuth provider returned an error. */
    class ProviderError(
        val provider: AuthProvider,
        message: String = "Authentication failed with ${provider.name}",
        cause: Throwable? = null,
    ) : AuthException(message, cause)

    /** Firebase authentication failed. */
    class FirebaseError(
        message: String = "Firebase authentication failed",
        cause: Throwable? = null,
    ) : AuthException(message, cause)

    /** No ID token was returned. */
    class NoToken(
        message: String = "No ID token received from provider"
    ) : AuthException(message)
}
