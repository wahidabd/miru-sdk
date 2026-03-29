package com.miru.sdk.auth.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.miru.sdk.auth.AuthProvider
import com.miru.sdk.auth.AuthResult
import com.miru.sdk.core.AppException
import com.miru.sdk.core.Resource
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import io.github.aakira.napier.Napier

/**
 * Pre-built Google Sign-In button — standalone OAuth (no Firebase needed).
 *
 * Shows Google One Tap popup and returns the user's idToken, email, etc.
 * Send the idToken to your own backend for verification.
 *
 * Usage:
 * ```
 * // 1. Initialize once at app startup
 * MiruGoogleAuth.initialize(serverClientId = "YOUR_SERVER_CLIENT_ID")
 *
 * // 2. Use in Composable
 * MiruGoogleSignInButton { resource ->
 *     resource.onSuccess { auth ->
 *         // Send auth.idToken to your backend API
 *         api.loginWithGoogle(auth.idToken!!)
 *     }
 * }
 * ```
 */
@Composable
fun MiruGoogleSignInButton(
    modifier: Modifier = Modifier,
    onResult: (Resource<AuthResult>) -> Unit,
) {
    GoogleButtonUiContainer(
        onGoogleSignInResult = { googleUser ->
            if (googleUser != null) {
                val result = AuthResult(
                    idToken = googleUser.idToken,
                    accessToken = googleUser.accessToken,
                    displayName = googleUser.displayName,
                    email = googleUser.email,
                    photoUrl = googleUser.profilePicUrl,
                    provider = AuthProvider.GOOGLE,
                )
                Napier.d("Google sign-in success: ${googleUser.email}")
                onResult(Resource.Success(result))
            } else {
                Napier.e("Google sign-in: null user returned")
                onResult(Resource.Error(AppException.UnknownException(throwable = Exception("Google sign-in returned null"))))
            }
        },
    ) {
        GoogleSignInButton(modifier = modifier.fillMaxWidth()) {
            this.onClick()
        }
    }
}
