package com.miru.sdk.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.miru.sdk.auth.AuthResult
import com.miru.sdk.auth.google.MiruGoogleAuth
import com.miru.sdk.core.Resource
import com.mmk.kmpauth.google.GoogleButtonMode
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import io.github.aakira.napier.Napier

/**
 * Pre-built Google Sign-In button using KMPAuth's native UI.
 *
 * Handles the full OAuth flow and returns [Resource<AuthResult>] via callback.
 *
 * Usage:
 * ```
 * MiruGoogleSignInButton(
 *     onResult = { resource ->
 *         resource
 *             .onSuccess { auth -> println("Signed in: ${auth.email}") }
 *             .onError { e, _ -> println("Error: ${e.message}") }
 *     }
 * )
 * ```
 */
@Composable
fun MiruGoogleSignInButton(
    modifier: Modifier = Modifier,
    mode: GoogleButtonMode = GoogleButtonMode.POPUP,
    onResult: (Resource<AuthResult>) -> Unit,
) {
    val googleAuth = MiruGoogleAuth()

    GoogleSignInButton(
        modifier = modifier,
        mode = mode,
        onGoogleSignInResult = { googleUser ->
            val result = if (googleUser != null) {
                googleAuth.handleResult(
                    idToken = googleUser.idToken,
                    displayName = googleUser.displayName,
                    email = googleUser.email,
                    photoUrl = googleUser.profilePicUrl,
                )
            } else {
                googleAuth.handleError(Exception("Google sign-in returned null user"))
            }
            onResult(result)
        },
    )
}
