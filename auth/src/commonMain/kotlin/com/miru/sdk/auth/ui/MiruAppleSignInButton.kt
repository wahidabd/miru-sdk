package com.miru.sdk.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.miru.sdk.auth.AuthResult
import com.miru.sdk.auth.apple.MiruAppleAuth
import com.miru.sdk.core.Resource
import com.mmk.kmpauth.firebase.apple.AppleButtonMode
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import io.github.aakira.napier.Napier

/**
 * Pre-built Apple Sign-In button using KMPAuth's native UI.
 *
 * Requires Firebase Auth configured in your project.
 *
 * Usage:
 * ```
 * MiruAppleSignInButton(
 *     onResult = { resource ->
 *         resource
 *             .onSuccess { auth -> println("Signed in: ${auth.email}") }
 *             .onError { e, _ -> println("Error: ${e.message}") }
 *     }
 * )
 * ```
 */
@Composable
fun MiruAppleSignInButton(
    modifier: Modifier = Modifier,
    onResult: (Resource<AuthResult>) -> Unit,
) {
    val appleAuth = MiruAppleAuth()

    AppleSignInButton(
        modifier = modifier,
    ) { result ->
        result.onSuccess { firebaseUser ->
            val authResult = appleAuth.handleResult(
                idToken = firebaseUser?.uid,
                displayName = firebaseUser?.displayName,
                email = firebaseUser?.email,
                photoUrl = firebaseUser?.photoUrl,
            )
            onResult(authResult)
        }
        result.onFailure { error ->
            onResult(appleAuth.handleError(error))
        }
    }
}
