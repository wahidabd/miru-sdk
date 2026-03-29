package com.miru.sdk.auth.apple

import com.miru.sdk.auth.AuthProvider
import com.miru.sdk.auth.AuthResult
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AuthenticationServices.ASAuthorization
import platform.AuthenticationServices.ASAuthorizationAppleIDCredential
import platform.AuthenticationServices.ASAuthorizationAppleIDProvider
import platform.AuthenticationServices.ASAuthorizationController
import platform.AuthenticationServices.ASAuthorizationControllerDelegateProtocol
import platform.AuthenticationServices.ASAuthorizationControllerPresentationContextProvidingProtocol
import platform.AuthenticationServices.ASAuthorizationScopeEmail
import platform.AuthenticationServices.ASAuthorizationScopeFullName
import platform.AuthenticationServices.ASPresentationAnchor
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.darwin.NSObject
import kotlin.coroutines.resume

actual class MiruAppleAuth actual constructor() {

    actual fun isAvailable(): Boolean = true

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun signIn(): AuthResult? = suspendCancellableCoroutine { continuation ->
        val provider = ASAuthorizationAppleIDProvider()
        val request = provider.createRequest()
        request.requestedScopes = listOf(
            ASAuthorizationScopeFullName,
            ASAuthorizationScopeEmail,
        )

        val delegate = AppleSignInDelegate { result ->
            if (continuation.isActive) {
                continuation.resume(result)
            }
        }

        val controller = ASAuthorizationController(
            authorizationRequests = listOf(request)
        )
        controller.delegate = delegate
        controller.presentationContextProvider = delegate
        controller.performRequests()

        continuation.invokeOnCancellation { /* no-op */ }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class AppleSignInDelegate(
    private val onComplete: (AuthResult?) -> Unit,
) : NSObject(),
    ASAuthorizationControllerDelegateProtocol,
    ASAuthorizationControllerPresentationContextProvidingProtocol {

    override fun authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithAuthorization: ASAuthorization,
    ) {
        val credential = didCompleteWithAuthorization.credential
        if (credential is ASAuthorizationAppleIDCredential) {
            val idTokenData = credential.identityToken
            val idToken = idTokenData?.let {
                NSString.create(data = it, encoding = NSUTF8StringEncoding) as? String
            }

            val fullName = credential.fullName
            val displayName = listOfNotNull(
                fullName?.givenName,
                fullName?.familyName,
            ).joinToString(" ").ifBlank { null }

            onComplete(
                AuthResult(
                    idToken = idToken,
                    displayName = displayName,
                    email = credential.email,
                    provider = AuthProvider.APPLE,
                )
            )
        } else {
            onComplete(null)
        }
    }

    override fun authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithError: NSError,
    ) {
        Napier.e("Apple Sign-In error: ${didCompleteWithError.localizedDescription}")
        onComplete(null)
    }

    @Suppress("DEPRECATION")
    override fun presentationAnchorForAuthorizationController(
        controller: ASAuthorizationController,
    ): ASPresentationAnchor {
        // Use the key window from the first connected scene (iOS 15+),
        // falling back to the deprecated keyWindow for older iOS.
        val scenes = UIApplication.sharedApplication.connectedScenes
        val windowScene = scenes.firstOrNull { it is UIWindowScene } as? UIWindowScene
        val window = windowScene?.windows?.firstOrNull { (it as? UIWindow)?.isKeyWindow() == true } as? UIWindow
        return window ?: (UIApplication.sharedApplication.keyWindow ?: UIWindow())
    }
}
