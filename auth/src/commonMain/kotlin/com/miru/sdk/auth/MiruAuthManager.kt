package com.miru.sdk.auth

import com.miru.sdk.core.Resource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Central auth manager that tracks the current authentication state.
 *
 * Provider-agnostic — works with any sign-in flow that produces an [AuthResult].
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
 * // Handle sign-in result from any button
 * MiruGoogleSignInButton { resource ->
 *     authManager.handleSignInResult(resource)
 * }
 *
 * // Sign out
 * authManager.signOut()
 * ```
 */
class MiruAuthManager {

    private val _currentUser = MutableStateFlow<AuthResult?>(null)

    /** Reactive state of the currently authenticated user. Null = not signed in. */
    val currentUser: StateFlow<AuthResult?> = _currentUser.asStateFlow()

    /** Whether a user is currently signed in. */
    val isSignedIn: Boolean get() = _currentUser.value != null

    /**
     * Set the auth result after a successful sign-in.
     */
    fun setAuthResult(result: AuthResult) {
        _currentUser.value = result
        Napier.d("Auth: ${result.provider} — ${result.email}")
    }

    /**
     * Handle a [Resource<AuthResult>] from any sign-in flow.
     * Auto-updates [currentUser] on success.
     */
    fun handleSignInResult(resource: Resource<AuthResult>): Resource<AuthResult> {
        when (resource) {
            is Resource.Success -> setAuthResult(resource.data)
            is Resource.Error -> Napier.e("Sign-in failed: ${resource.exception.message}")
            is Resource.Loading -> { /* no-op */ }
        }
        return resource
    }

    /** Clear auth state. */
    fun signOut() {
        val prev = _currentUser.value
        _currentUser.value = null
        Napier.d("Signed out from ${prev?.provider?.name ?: "none"}")
    }
}
