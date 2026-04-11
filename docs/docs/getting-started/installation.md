---
sidebar_position: 1
title: Installation
---

# Installation

Miru SDK is published to Maven Central — no extra repository configuration needed. `mavenCentral()` is already included by default in Gradle.

## Kotlin Multiplatform

### All-in-One (Recommended)

Use the umbrella module to get every module in a single dependency:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.wahidabd:miru-sdk:<version>")
        }
    }
}
```

### Individual Modules

Pick only what you need:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.wahidabd:miru-sdk-core:<version>")
            implementation("io.github.wahidabd:miru-sdk-network:<version>")
            implementation("io.github.wahidabd:miru-sdk-ui-state:<version>")
            // ... add more as needed
        }
    }
}
```

## Android Only

If your project is Android-only:

```kotlin
dependencies {
    implementation("io.github.wahidabd:miru-sdk:<version>")
}
```

## Available Modules

| Artifact ID | Description |
|---|---|
| `miru-sdk` | Umbrella — includes everything below |
| `miru-sdk-core` | Resource, AppException, extensions |
| `miru-sdk-network` | Ktor HTTP client, token management |
| `miru-sdk-ui-state` | BaseViewModel, UiState, EventFlow |
| `miru-sdk-ui-components` | Compose UI components, theming |
| `miru-sdk-navigation` | Navigation wrapper, transitions |
| `miru-sdk-di` | Koin DI, SDK initializer |
| `miru-sdk-firebase` | Remote Config, FCM topics |
| `miru-sdk-auth` | Google, Apple, Facebook OAuth |
| `miru-sdk-persistent` | Room KMP, DataStore |

:::tip
Replace `<version>` with the latest release from [Maven Central](https://central.sonatype.com/artifact/io.github.wahidabd/miru-sdk).
:::

## Requirements

- Kotlin 2.3.0+
- AGP 9.0.0+
- Android `minSdk` 24, `compileSdk` 36
- iOS deployment target 16.0+
- Compose Multiplatform 1.10.0+
