package com.miru.sdk.auth.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.miru.sdk.auth.AuthResult
import com.miru.sdk.auth.facebook.MiruFacebookAuth
import com.miru.sdk.core.Resource
import com.mmk.kmpauth.firebase.facebook.FacebookButtonMode
import com.mmk.kmpauth.uihelper.facebook.FacebookSignInButton
import io.github.aakira.napier.Napier

/**
 * Pre-built Facebook Sign-In button using KMPAuth's native UI.
 *
 * Requires Firebase Auth configured in your project.
 *
 * Usage:
 * ```
 * MiruFacebookSignInButton(
 *     onResult = { resource ->
 *         resource
 *             .onSuccess { auth -> println("Signed in: ${auth.email}") }
 *             .onError { e, _ -> println("Error: ${e.message}") }
 *     }
 * )
 * ```
 */
@Composable
fun MiruFacebookSignInButton(
    modifier: Modifier = Modifier,
    onResult: (Resource<AuthResult>) -> Unit,
) {
    val facebookAuth = MiruFacebookAuth()

    FacebookSignInButton(
        modifier = modifier,
    ) { result ->
        result.onSuccess { firebaseUser ->
            val authResult = facebookAuth.handleResult(
                accessToken = firebaseUser?.uid,
                displayName = firebaseUser?.displayName,
                email = firebaseUser?.email,
                photoUrl = firebaseUser?.photoUrl,
            )
            onResult(authResult)
        }
        result.onFailure { error ->
            onResult(facebookAuth.handleError(error))
        }
    }
}
