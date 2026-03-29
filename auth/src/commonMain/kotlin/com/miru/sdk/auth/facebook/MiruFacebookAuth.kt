package com.miru.sdk.auth.facebook

import com.miru.sdk.auth.AuthResult

/**
 * Miru Facebook Auth — native Facebook Login for Android & iOS.
 *
 * Returns the user's `accessToken` from Facebook.
 * Send the accessToken to your backend API for verification.
 *
 * **Setup required:**
 * - Android: Add Facebook App ID in `AndroidManifest.xml` and `strings.xml`
 * - iOS: Add Facebook App ID in `Info.plist`
 *
 * Usage:
 * ```
 * val facebookAuth = MiruFacebookAuth()
 * val result: AuthResult? = facebookAuth.signIn()
 * if (result != null) {
 *     api.loginWithFacebook(result.accessToken!!)
 * }
 * ```
 */
expect class MiruFacebookAuth() {
    /**
     * Launch the Facebook Login popup.
     *
     * @return [AuthResult] with accessToken on success. Null on cancel/error.
     */
    suspend fun signIn(): AuthResult?

    /**
     * Sign out from Facebook.
     */
    fun signOut()
}
