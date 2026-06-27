# Chucker HTTP Inspector Integration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Integrate Chucker as a built-in Android debug HTTP inspector into miru-sdk, activated automatically with zero consumer configuration beyond using the Android-specific initializer.

**Architecture:** Extend `HttpEngineFactory` (expect/actual) to accept a `List<Any>` of interceptors; on Android the actual implementation casts and injects them into OkHttp. The `di` module's `PlatformModule.android.kt` provides `ChuckerInterceptor` as a Koin singleton using `androidContext()`; `NetworkModule` picks it up by name and forwards it to `HttpClientFactory`. A new Android-specific extension function on `MiruSdkInitializer` calls `androidContext(context)` in `startKoin` so that `androidContext()` resolves correctly inside Koin modules.

**Tech Stack:** Kotlin Multiplatform, Ktor 3.4.1, OkHttp (via `ktor-client-okhttp`), Koin 4.1.0, Chucker 4.0.0 (debug) / Chucker no-op 4.0.0 (release)

## Global Constraints

- minSdk 24, compileSdk 36
- Chucker only active in Android debug builds; release uses `library-no-op` (same API, zero overhead)
- iOS behavior must remain unchanged — `createHttpEngine` iOS actual ignores the interceptors param
- All new Android-only code lives in `androidMain` source sets; no Android types in `commonMain`
- Koin qualifier for interceptors: `named("networkInterceptors")` — used consistently across PlatformModule and NetworkModule

---

## File Map

| Action | File |
|--------|------|
| Modify | `gradle/libs.versions.toml` |
| Modify | `di/build.gradle.kts` |
| Modify | `network/src/commonMain/kotlin/com/miru/sdk/network/client/HttpEngineFactory.kt` |
| Modify | `network/src/androidMain/kotlin/com/miru/sdk/network/client/HttpEngineFactory.android.kt` |
| Modify | `network/src/iosMain/kotlin/com/miru/sdk/network/client/HttpEngineFactory.ios.kt` |
| Modify | `network/src/commonMain/kotlin/com/miru/sdk/network/client/HttpClientFactory.kt` |
| Modify | `di/src/androidMain/kotlin/com/miru/sdk/di/modules/PlatformModule.android.kt` |
| Modify | `di/src/iosMain/kotlin/com/miru/sdk/di/modules/PlatformModule.ios.kt` |
| Modify | `di/src/commonMain/kotlin/com/miru/sdk/di/modules/NetworkModule.kt` |
| Create | `di/src/androidMain/kotlin/com/miru/sdk/di/MiruSdkInitializerAndroid.kt` |
| Create | `network/src/commonTest/kotlin/com/miru/sdk/network/client/HttpClientFactoryTest.kt` |

---

## Task 1: Extend `HttpEngineFactory` and `HttpClientFactory` to support interceptors

**Files:**
- Modify: `network/src/commonMain/kotlin/com/miru/sdk/network/client/HttpEngineFactory.kt`
- Modify: `network/src/androidMain/kotlin/com/miru/sdk/network/client/HttpEngineFactory.android.kt`
- Modify: `network/src/iosMain/kotlin/com/miru/sdk/network/client/HttpEngineFactory.ios.kt`
- Modify: `network/src/commonMain/kotlin/com/miru/sdk/network/client/HttpClientFactory.kt`
- Create: `network/src/commonTest/kotlin/com/miru/sdk/network/client/HttpClientFactoryTest.kt`

**Interfaces:**
- Produces: `createHttpEngine(interceptors: List<Any>): HttpClientEngine` (expect/actual)
- Produces: `HttpClientFactory.create(config, tokenProvider, interceptors): HttpClient`

- [ ] **Step 1: Write the failing test**

Create `network/src/commonTest/kotlin/com/miru/sdk/network/client/HttpClientFactoryTest.kt`:

```kotlin
package com.miru.sdk.network.client

import com.miru.sdk.network.config.NetworkConfig
import kotlin.test.Test
import kotlin.test.assertNotNull

class HttpClientFactoryTest {

    private val config = NetworkConfig(
        baseUrl = "https://api.example.com",
        enableLogging = false
    )

    @Test
    fun `create returns HttpClient when called with default empty interceptors`() {
        val client = HttpClientFactory.create(config)
        assertNotNull(client)
        client.close()
    }

    @Test
    fun `create returns HttpClient when called with explicit empty interceptors list`() {
        val client = HttpClientFactory.create(config, interceptors = emptyList())
        assertNotNull(client)
        client.close()
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

```bash
./gradlew :network:compileCommonMainKotlinMetadata
```

Expected: compile error — `HttpClientFactory.create` does not have `interceptors` parameter yet.

- [ ] **Step 3: Update `HttpEngineFactory.kt` (commonMain expect)**

Replace the entire file content:

```kotlin
package com.miru.sdk.network.client

import io.ktor.client.engine.HttpClientEngine

expect fun createHttpEngine(interceptors: List<Any> = emptyList()): HttpClientEngine
```

- [ ] **Step 4: Update `HttpEngineFactory.android.kt`**

Replace the entire file content:

```kotlin
package com.miru.sdk.network.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.Interceptor

actual fun createHttpEngine(interceptors: List<Any>): HttpClientEngine {
    return OkHttp.create {
        config {
            interceptors.filterIsInstance<Interceptor>().forEach { interceptor ->
                addInterceptor(interceptor)
            }
        }
    }
}
```

- [ ] **Step 5: Update `HttpEngineFactory.ios.kt`**

Replace the entire file content:

```kotlin
package com.miru.sdk.network.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun createHttpEngine(interceptors: List<Any>): HttpClientEngine {
    return Darwin.create()
}
```

- [ ] **Step 6: Update `HttpClientFactory.kt`**

Replace the `create` function signature and its first line — full file:

```kotlin
package com.miru.sdk.network.client

import com.miru.sdk.network.config.LogLevel
import com.miru.sdk.network.config.NetworkConfig
import com.miru.sdk.network.token.TokenProvider
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel as KtorLogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(
        config: NetworkConfig,
        tokenProvider: TokenProvider? = null,
        interceptors: List<Any> = emptyList()
    ): HttpClient {
        return HttpClient(createHttpEngine(interceptors)) {
            install(HttpTimeout) {
                connectTimeoutMillis = config.connectTimeout
                requestTimeoutMillis = config.requestTimeout
                socketTimeoutMillis = config.socketTimeout
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                    }
                )
            }

            if (config.enableLogging) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Napier.d(tag = "HttpClient", message = message)
                        }
                    }
                    level = mapLogLevel(config.logLevel)
                }
            }

            install(DefaultRequest) {
                url(config.baseUrl)
                contentType(ContentType.Application.Json)
                header("Accept", ContentType.Application.Json)
            }
        }
    }

    private fun mapLogLevel(logLevel: LogLevel): KtorLogLevel {
        return when (logLevel) {
            LogLevel.NONE -> KtorLogLevel.NONE
            LogLevel.HEADERS -> KtorLogLevel.HEADERS
            LogLevel.BODY -> KtorLogLevel.BODY
            LogLevel.ALL -> KtorLogLevel.ALL
        }
    }
}
```

- [ ] **Step 7: Run tests to verify they pass**

```bash
./gradlew :network:allTests
```

Expected: `HttpClientFactoryTest` — 2 tests PASSED.

- [ ] **Step 8: Commit**

```bash
git add network/src/commonMain/kotlin/com/miru/sdk/network/client/HttpEngineFactory.kt \
        network/src/androidMain/kotlin/com/miru/sdk/network/client/HttpEngineFactory.android.kt \
        network/src/iosMain/kotlin/com/miru/sdk/network/client/HttpEngineFactory.ios.kt \
        network/src/commonMain/kotlin/com/miru/sdk/network/client/HttpClientFactory.kt \
        network/src/commonTest/kotlin/com/miru/sdk/network/client/HttpClientFactoryTest.kt
git commit -m "feat(network): add interceptors support to HttpEngineFactory and HttpClientFactory"
```

---

## Task 2: Add Chucker dependency and Android-specific initializer

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `di/build.gradle.kts`
- Create: `di/src/androidMain/kotlin/com/miru/sdk/di/MiruSdkInitializerAndroid.kt`

**Interfaces:**
- Consumes: `MiruSdkInitializer` object, `MiruSdkConfig`, `coreModule()`, `networkModule()`, `platformModule()` — all already exist
- Produces: `fun MiruSdkInitializer.initialize(context: android.content.Context, config: MiruSdkConfig)` — extension function available only on Android

- [ ] **Step 1: Add Chucker to `gradle/libs.versions.toml`**

In the `[versions]` section, add after the `napier` line:

```toml
chucker = "4.0.0"
```

In the `[libraries]` section, add at the end:

```toml
chucker = { module = "com.github.chuckerteam.chucker:library", version.ref = "chucker" }
chucker-no-op = { module = "com.github.chuckerteam.chucker:library-no-op", version.ref = "chucker" }
```

- [ ] **Step 2: Add Chucker deps to `di/build.gradle.kts`**

Append a top-level `dependencies` block at the end of the file (after the `kotlin { }` block):

```kotlin
dependencies {
    "debugImplementation"(libs.chucker)
    "releaseImplementation"(libs.chucker.no.op)
}
```

Full final `di/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("publish")
}

kotlin {
    androidLibrary {
        namespace = "com.miru.sdk.di"
        compileSdk = 36
        minSdk = 24
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":network"))
            implementation(libs.ktor.client.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    "debugImplementation"(libs.chucker)
    "releaseImplementation"(libs.chucker.no.op)
}
```

- [ ] **Step 3: Sync Gradle**

```bash
./gradlew :di:dependencies --configuration debugRuntimeClasspath | grep chucker
```

Expected: line containing `com.github.chuckerteam.chucker:library:4.0.0`

- [ ] **Step 4: Create `MiruSdkInitializerAndroid.kt`**

Create `di/src/androidMain/kotlin/com/miru/sdk/di/MiruSdkInitializerAndroid.kt`:

```kotlin
package com.miru.sdk.di

import android.content.Context
import com.miru.sdk.core.logger.MiruLogger
import com.miru.sdk.di.modules.coreModule
import com.miru.sdk.di.modules.networkModule
import com.miru.sdk.di.modules.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun MiruSdkInitializer.initialize(context: Context, config: MiruSdkConfig) {
    if (config.enableLogging) {
        MiruLogger.init()
        MiruLogger.d(tag = "MiruSdkInitializer", message = "Initializing Miru SDK")
    }

    val modules = mutableListOf<Module>()
    modules.add(coreModule())
    modules.add(networkModule(config.networkConfig, config.tokenProvider))
    modules.add(platformModule())
    modules.addAll(config.additionalModules)

    startKoin {
        androidContext(context)
        modules(modules)
    }

    if (config.enableLogging) {
        MiruLogger.d(tag = "MiruSdkInitializer", message = "Miru SDK initialized successfully")
    }
}
```

- [ ] **Step 5: Verify compilation**

```bash
./gradlew :di:compileDebugKotlinAndroid
```

Expected: BUILD SUCCESSFUL — no errors.

- [ ] **Step 6: Commit**

```bash
git add gradle/libs.versions.toml \
        di/build.gradle.kts \
        di/src/androidMain/kotlin/com/miru/sdk/di/MiruSdkInitializerAndroid.kt
git commit -m "feat(di): add Chucker dependency and Android-specific SDK initializer"
```

---

## Task 3: Wire Chucker into Koin via PlatformModule and NetworkModule

**Files:**
- Modify: `di/src/androidMain/kotlin/com/miru/sdk/di/modules/PlatformModule.android.kt`
- Modify: `di/src/iosMain/kotlin/com/miru/sdk/di/modules/PlatformModule.ios.kt`
- Modify: `di/src/commonMain/kotlin/com/miru/sdk/di/modules/NetworkModule.kt`

**Interfaces:**
- Consumes: `HttpClientFactory.create(config, tokenProvider, interceptors)` from Task 1
- Consumes: `androidContext()` from `koin-android`
- Consumes: `named("networkInterceptors")` Koin qualifier — string must match exactly in both PlatformModule and NetworkModule
- Produces: `HttpClient` singleton in Koin with Chucker active on debug, no-op on release, and empty on iOS

- [ ] **Step 1: Update `PlatformModule.android.kt`**

Replace the entire file content:

```kotlin
package com.miru.sdk.di.modules

import com.chuckerteam.chucker.api.ChuckerInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<List<Any>>(named("networkInterceptors")) {
        listOf(
            ChuckerInterceptor.Builder(androidContext()).build()
        )
    }
}
```

- [ ] **Step 2: Update `PlatformModule.ios.kt`**

Replace the entire file content:

```kotlin
package com.miru.sdk.di.modules

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<List<Any>>(named("networkInterceptors")) {
        emptyList()
    }
}
```

- [ ] **Step 3: Update `NetworkModule.kt`**

Replace the `single<HttpClient>` block — full file:

```kotlin
package com.miru.sdk.di.modules

import com.miru.sdk.network.client.HttpClientFactory
import com.miru.sdk.network.config.NetworkConfig
import com.miru.sdk.network.token.TokenProvider
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun networkModule(
    networkConfig: NetworkConfig,
    tokenProvider: TokenProvider?
): Module = module {
    single<NetworkConfig> { networkConfig }

    tokenProvider?.let { provider ->
        single<TokenProvider> { provider }
    }

    single<HttpClient> {
        val interceptors = getOrNull<List<Any>>(named("networkInterceptors")) ?: emptyList()
        HttpClientFactory.create(
            config = networkConfig,
            tokenProvider = tokenProvider,
            interceptors = interceptors
        )
    }
}
```

- [ ] **Step 4: Verify Android debug build compiles**

```bash
./gradlew :di:compileDebugKotlinAndroid
```

Expected: BUILD SUCCESSFUL.

- [ ] **Step 5: Verify iOS build compiles**

```bash
./gradlew :di:compileKotlinIosArm64
```

Expected: BUILD SUCCESSFUL.

- [ ] **Step 6: Run full build**

```bash
./gradlew build
```

Expected: BUILD SUCCESSFUL — all modules compile for all targets.

- [ ] **Step 7: Manual smoke test on sample app (Android)**

In `sample/app`, ensure the `Application` class calls the Android initializer:

```kotlin
// sample/app/src/main/kotlin/.../SampleApplication.kt
MiruSdkInitializer.initialize(this, config)
```

Run the sample app on an Android device or emulator in **debug** mode. Make any network request. Expected: Chucker notification appears in the status bar showing the HTTP request.

Run again in **release** mode. Expected: no Chucker notification, request proceeds normally.

- [ ] **Step 8: Commit**

```bash
git add di/src/androidMain/kotlin/com/miru/sdk/di/modules/PlatformModule.android.kt \
        di/src/iosMain/kotlin/com/miru/sdk/di/modules/PlatformModule.ios.kt \
        di/src/commonMain/kotlin/com/miru/sdk/di/modules/NetworkModule.kt
git commit -m "feat(di): wire Chucker interceptor through Koin PlatformModule and NetworkModule"
```
