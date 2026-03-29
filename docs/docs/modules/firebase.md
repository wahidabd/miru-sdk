---
sidebar_position: 7
title: Firebase
---

# Firebase Module

Firebase KMP integration via GitLive, providing Remote Config and FCM topic management.

```kotlin
implementation("com.github.wahidabd.miru-sdk:firebase:<version>")
```

:::caution
The Firebase module requires core library desugaring on Android. Add this to your app-level `build.gradle.kts`:

```kotlin
android {
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")
}
```
:::

## Remote Config

Fetch and read Firebase Remote Config values:

```kotlin
val config: MiruRemoteConfig = get() // via Koin

// Set defaults before fetching
config.setDefaults(mapOf(
    "feature_new_ui" to false,
    "api_base_url" to "https://api.yourapp.com",
    "max_retry" to 3L
))

// Fetch & activate
config.fetchAndActivate().collect { resource ->
    resource.onSuccess { activated -> println("Config activated: $activated") }
}

// Read values
val featureEnabled = config.getBoolean("feature_new_ui")
val apiUrl = config.getString("api_base_url")
val maxRetry = config.getLong("max_retry")
```

## FCM Topic Management

Subscribe, unsubscribe, and observe FCM topics reactively:

```kotlin
val topicManager: TopicManager = get() // via Koin

// Subscribe to topics
topicManager.subscribe("promo")
topicManager.subscribeAll(listOf("news", "updates", "alerts"))

// Observe active subscriptions
topicManager.subscribedTopics.collect { topics ->
    println("Subscribed to: $topics")
}

// Check & unsubscribe
if (topicManager.isSubscribed("promo")) {
    topicManager.unsubscribe("promo")
}
```

## Koin Setup

```kotlin
startKoin {
    modules(
        firebaseModule, // provides MiruRemoteConfig, MiruMessaging, TopicManager
        // ... other modules
    )
}
```

Or if using `MiruSdkInitializer`, the firebase module is automatically included.
