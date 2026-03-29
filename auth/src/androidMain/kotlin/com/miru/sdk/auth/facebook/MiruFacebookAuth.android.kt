package com.miru.sdk.auth.facebook

import androidx.activity.result.ActivityResultRegistryOwner
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.miru.sdk.auth.AuthProvider
import com.miru.sdk.auth.AuthResult
import io.github.aakira.napier.Napier
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class MiruFacebookAuth actual constructor() {

    private val callbackManager = CallbackManager.Factory.create()
    private val loginManager = LoginManager.getInstance()

    companion object {
        private var registryOwnerRef: ActivityResultRegistryOwner? = null

        /**
         * Set the [ActivityResultRegistryOwner] before calling [signIn].
         * In most cases this is your `ComponentActivity`.
         *
         * ```
         * // In your Activity's onCreate
         * MiruFacebookAuth.setActivityResultRegistryOwner(this)
         * ```
         */
        fun setActivityResultRegistryOwner(owner: ActivityResultRegistryOwner) {
            registryOwnerRef = owner
        }

        /**
         * Clear the reference to prevent memory leaks.
         * Call this from your Activity's `onDestroy`.
         */
        fun clearActivityResultRegistryOwner() {
            registryOwnerRef = null
        }
    }

    actual suspend fun signIn(): AuthResult? {
        val owner = registryOwnerRef
        if (owner == null) {
            Napier.e("Facebook login failed: ActivityResultRegistryOwner not set. Call MiruFacebookAuth.setActivityResultRegistryOwner(activity) first.")
            return null
        }

        return suspendCancellableCoroutine { continuation ->
            loginManager.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        val accessToken = result.accessToken
                        fetchProfile(accessToken) { authResult ->
                            if (continuation.isActive) {
                                continuation.resume(authResult)
                            }
                        }
                    }

                    override fun onCancel() {
                        Napier.d("Facebook login cancelled")
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }

                    override fun onError(error: FacebookException) {
                        Napier.e("Facebook login error", error)
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }
                }
            )

            loginManager.logInWithReadPermissions(
                owner,
                callbackManager,
                listOf("email", "public_profile"),
            )
        }
    }

    private fun fetchProfile(accessToken: AccessToken, onResult: (AuthResult) -> Unit) {
        val request = GraphRequest.newMeRequest(accessToken) { jsonObject, _ ->
            val result = AuthResult(
                accessToken = accessToken.token,
                displayName = jsonObject?.optString("name"),
                email = jsonObject?.optString("email"),
                photoUrl = jsonObject?.optJSONObject("picture")
                    ?.optJSONObject("data")
                    ?.optString("url"),
                provider = AuthProvider.FACEBOOK,
            )
            onResult(result)
        }
        request.parameters = android.os.Bundle().apply {
            putString("fields", "id,name,email,picture.type(large)")
        }
        request.executeAsync()
    }

    actual fun signOut() {
        loginManager.logOut()
        Napier.d("Facebook signed out")
    }
}
