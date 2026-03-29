---
sidebar_position: 8
title: Auth
---

# Auth Module

Social authentication with Google, Apple, and Facebook OAuth, including pre-built Compose sign-in buttons.

```kotlin
implementation("com.github.wahidabd.miru-sdk:auth:<version>")
```

## Google Sign-In

Standalone One Tap authentication — returns `idToken` directly (no Firebase dependency):

```kotlin
// 1. Initialize once at app startup
MiruGoogleAuth.initialize(serverClientId = "YOUR_SERVER_CLIENT_ID")

// 2. Pre-built Compose button
MiruGoogleSignInButton { resource ->
    resource.onSuccess { auth ->
        // Send idToken to your backend API
        api.loginWithGoogle(auth.idToken!!)
    }
}
```

## Apple Sign-In

Native iOS only — shows the system Apple ID popup and returns `identityToken`:

```kotlin
val appleAuth: MiruAppleAuth = get()

if (appleAuth.isAvailable()) {
    val result = appleAuth.signIn()
    result?.let { auth ->
        // auth.idToken = Apple identityToken
        // auth.email, auth.displayName
        api.loginWithApple(auth.idToken!!)
    }
}
```

## Facebook Login

Uses the native Facebook SDK:

```kotlin
// Android: set ActivityResultRegistryOwner in your Activity's onCreate
MiruFacebookAuth.setActivityResultRegistryOwner(this)

val facebookAuth: MiruFacebookAuth = get()

val result = facebookAuth.signIn()
result?.let { auth ->
    // auth.accessToken = Facebook access token
    // auth.email, auth.displayName, auth.photoUrl
    api.loginWithFacebook(auth.accessToken!!)
}

// Android: clear in onDestroy to prevent leaks
MiruFacebookAuth.clearActivityResultRegistryOwner()
```

## MiruAuthManager

Centralized, provider-agnostic auth state management:

```kotlin
val authManager: MiruAuthManager = get()

// Observe auth state reactively
authManager.currentUser.collect { user ->
    if (user != null) navigateToHome()
    else navigateToLogin()
}

// Handle any sign-in result
MiruGoogleSignInButton { resource ->
    authManager.handleSignInResult(resource)
}

// Sign out
authManager.signOut()
```

## Platform Support

| Provider | Platform | Implementation |
|---|---|---|
| Google | Android + iOS | KMPAuth standalone (commonMain) |
| Apple | iOS only | Native ASAuthorization (iosMain) |
| Facebook | Android + iOS | Facebook SDK (expect/actual) |
