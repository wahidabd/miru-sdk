package com.miru.sdk.auth.apple

import com.miru.sdk.auth.AuthResult

/**
 * Miru Apple Auth — native Apple Sign-In (iOS only).
 *
 * Returns the user's `identityToken`, `email`, and `fullName` from Apple.
 * Send the identityToken to your backend API for verification.
 *
 * Usage:
 * ```
 * val appleAuth = MiruAppleAuth()
 * val result: AuthResult? = appleAuth.signIn()
 * if (result != null) {
 *     api.loginWithApple(result.idToken!!)
 * }
 * ```
 *
 * Note: Only available on iOS. Returns `null` on Android.
 */
expect class MiruAppleAuth() {
    /**
     * Launch the Apple Sign-In popup.
     *
     * @return [AuthResult] with identityToken, email, displayName on success. Null on cancel/error.
     */
    suspend fun signIn(): AuthResult?

    /**
     * Whether Apple Sign-In is available on this platform.
     */
    fun isAvailable(): Boolean
}
