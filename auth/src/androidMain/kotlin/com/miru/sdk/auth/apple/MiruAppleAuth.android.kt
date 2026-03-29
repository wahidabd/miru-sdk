package com.miru.sdk.auth.apple

import com.miru.sdk.auth.AuthResult

/**
 * Apple Sign-In is not available on Android.
 * [isAvailable] returns false, [signIn] returns null.
 */
actual class MiruAppleAuth actual constructor() {

    actual fun isAvailable(): Boolean = false

    actual suspend fun signIn(): AuthResult? = null
}
