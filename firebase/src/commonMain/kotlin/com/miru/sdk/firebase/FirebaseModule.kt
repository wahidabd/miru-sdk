package com.miru.sdk.firebase

import com.miru.sdk.firebase.config.MiruRemoteConfig
import com.miru.sdk.firebase.messaging.MiruMessaging
import com.miru.sdk.firebase.messaging.TopicManager
import org.koin.dsl.module

/**
 * Koin module for Firebase dependencies.
 *
 * Usage in your app's Koin setup:
 * ```
 * startKoin {
 *     modules(
 *         firebaseModule,
 *         // ... other modules
 *     )
 * }
 * ```
 */
val firebaseModule = module {
    single { MiruRemoteConfig() }
    single { MiruMessaging() }
    single { TopicManager(get()) }
}
