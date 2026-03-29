package com.miru.sdk.auth

/**
 * Represents the result of an OAuth authentication attempt.
 *
 * @property idToken The ID token from the OAuth provider
 * @property accessToken The access token (if available)
 * @property displayName The user's display name
 * @property email The user's email address
 * @property photoUrl The user's profile photo URL
 * @property provider The OAuth provider that was used
 */
data class AuthResult(
    val idToken: String? = null,
    val accessToken: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val provider: AuthProvider,
)

/**
 * Supported OAuth providers.
 */
enum class AuthProvider {
    GOOGLE,
    APPLE,
    FACEBOOK,
}
