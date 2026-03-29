package com.miru.sdk.auth

import com.miru.sdk.auth.apple.MiruAppleAuth
import com.miru.sdk.auth.facebook.MiruFacebookAuth
import com.miru.sdk.auth.google.MiruGoogleAuth
import com.miru.sdk.core.Resource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Central auth manager that tracks the current authentication state.
 *
 * Works with [MiruGoogleAuth], [MiruAppleAuth], and [MiruFacebookAuth]
 * to provide a unified auth experience.
 *
 * Usage:
 * ```
 * val authManager: MiruAuthManager = get() // via Koin
 *
 * // Observe auth state
 * authManager.currentUser.collect { user ->
 *     if (user != null) navigateToHome()
 *     else navigateToLogin()
 * }
 *
 * // Set auth result from sign-in button callback
 * authManager.setAuthResult(authResult)
 *
 * // Sign out
 * authManager.signOut()
 * ```
 */
class MiruAuthManager(
    val googleAuth: MiruGoogleAuth,
    val appleAuth: MiruAppleAuth,
    val facebookAuth: MiruFacebookAuth,
) {
    private val _currentUser = MutableStateFlow<AuthResult?>(null)

    /**
     * Reactive state of the currently authenticated user.
     * Null means no user is signed in.
     */
    val currentUser: StateFlow<AuthResult?> = _currentUser.asStateFlow()

    /**
     * Whether a user is currently signed in.
     */
    val isSignedIn: Boolean get() = _currentUser.value != null

    /**
     * Set the auth result after a successful sign-in.
     * Typically called from sign-in button callbacks.
     */
    fun setAuthResult(result: AuthResult) {
        _currentUser.value = result
        Napier.d("Auth state updated: provider=${result.provider}, email=${result.email}")
    }

    /**
     * Handle a [Resource<AuthResult>] from any sign-in flow.
     * Automatically updates [currentUser] on success.
     *
     * @return the same Resource for chaining
     */
    fun handleSignInResult(resource: Resource<AuthResult>): Resource<AuthResult> {
        when (resource) {
            is Resource.Success -> setAuthResult(resource.data)
            is Resource.Error -> Napier.e("Sign-in failed: ${resource.exception.message}")
            is Resource.Loading -> { /* no-op */ }
        }
        return resource
    }

    /**
     * Clear the current authentication state.
     */
    fun signOut() {
        val previous = _currentUser.value
        _currentUser.value = null
        Napier.d("Signed out from ${previous?.provider?.name ?: "unknown"}")
    }
}
