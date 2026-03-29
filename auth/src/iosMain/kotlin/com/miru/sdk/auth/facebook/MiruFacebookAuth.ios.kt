package com.miru.sdk.auth.facebook

import com.miru.sdk.auth.AuthResult

/**
 * Facebook Login iOS implementation.
 *
 * TODO: Requires FacebookLogin pod in the consuming iOS app.
 * The consuming app should add the pod and implement the bridge.
 * For now, this returns null — implement when Facebook iOS SDK is integrated.
 */
actual class MiruFacebookAuth actual constructor() {

    actual suspend fun signIn(): AuthResult? {
        // iOS Facebook Login requires the FacebookLogin pod.
        // The consuming app needs to:
        // 1. Add pod 'FacebookLogin' to their Podfile
        // 2. Configure Info.plist with Facebook App ID
        // This stub can be replaced with native implementation
        // using FBSDKLoginManager via cinterop.
        return null
    }

    actual fun signOut() {
        // no-op until Facebook iOS SDK is integrated
    }
}
