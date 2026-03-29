---
sidebar_position: 1
title: Installation
---

# Installation

## Add JitPack Repository

Add JitPack to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

## Kotlin Multiplatform

### All-in-One (Recommended)

Use the umbrella module to get every module in a single dependency:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.github.wahidabd.miru-sdk:miru-sdk:<version>")
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
            implementation("com.github.wahidabd.miru-sdk:core:<version>")
            implementation("com.github.wahidabd.miru-sdk:network:<version>")
            implementation("com.github.wahidabd.miru-sdk:ui-state:<version>")
            // ... add more as needed
        }
    }
}
```

## Android Only

If your project is Android-only:

```kotlin
dependencies {
    implementation("com.github.wahidabd.miru-sdk:miru-sdk:<version>")
}
```

## Available Modules

| Artifact ID | Description |
|---|---|
| `miru-sdk` | Umbrella — includes everything below |
| `core` | Resource, AppException, extensions |
| `network` | Ktor HTTP client, token management |
| `ui-state` | BaseViewModel, UiState, EventFlow |
| `ui-components` | Compose UI components, theming |
| `navigation` | Navigation wrapper, transitions |
| `di` | Koin DI, SDK initializer |
| `firebase` | Remote Config, FCM topics |
| `auth` | Google, Apple, Facebook OAuth |
| `persistent` | Room KMP, DataStore |

:::tip
Replace `<version>` with the latest release tag from [JitPack](https://jitpack.io/#wahidabd/miru-sdk).
:::

## Requirements

- Kotlin 2.3.0+
- AGP 9.0.0+
- Android `minSdk` 24, `compileSdk` 36
- iOS deployment target 16.0+
- Compose Multiplatform 1.10.0+
