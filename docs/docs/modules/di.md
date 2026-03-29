---
sidebar_position: 6
title: DI
---

# DI Module

Dependency injection module providing SDK initialization, Koin module wiring, and Compose injection helpers.

```kotlin
implementation("com.github.wahidabd.miru-sdk:di:<version>")
```

## MiruSdkInitializer

Entry point for configuring the SDK. Call once at app startup:

```kotlin
MiruSdkInitializer.initialize(
    MiruSdkConfig(
        networkConfig = NetworkConfig(
            baseUrl = "https://api.yourapp.com/v1/",
            enableLogging = BuildConfig.DEBUG
        ),
        enableLogging = true,
        tokenProvider = MyTokenProvider(),
        additionalModules = listOf(
            userModule,
            productModule,
            settingsModule
        )
    )
)
```

### MiruSdkConfig

| Parameter | Type | Description |
|---|---|---|
| `networkConfig` | `NetworkConfig` | HTTP client configuration |
| `enableLogging` | `Boolean` | Enable Napier logging |
| `tokenProvider` | `TokenProvider?` | Auth token provider (optional) |
| `additionalModules` | `List<Module>` | Your app's Koin modules |

## Koin Modules

The SDK internally registers these Koin modules:

| Module | Provides |
|---|---|
| `coreModule` | DispatcherProvider, Logger |
| `networkModule` | HttpClient, HttpClientFactory |
| `platformModule` | Platform-specific implementations |

Your app modules are added alongside these via `additionalModules`.

## Compose Injection

Use standard Koin Compose APIs:

```kotlin
// ViewModel injection
@Composable
fun UserScreen(viewModel: UserViewModel = koinViewModel()) {
    // ...
}

// Regular dependency injection
@Composable
fun MyComponent() {
    val repository: UserRepository = koinInject()
}
```

## Organizing Koin Modules

Recommended pattern — one module per feature:

```kotlin
val userModule = module {
    // Data layer
    single { UserMapper() }
    single { UserApi(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    // Domain layer
    factory { GetUsersUseCase(get()) }
    factory { GetUserByIdUseCase(get()) }

    // Presentation layer
    viewModel { UserListViewModel(get()) }
    viewModel { UserDetailViewModel(get()) }
}
```

Then pass all modules at initialization:

```kotlin
MiruSdkInitializer.initialize(
    MiruSdkConfig(
        // ...
        additionalModules = listOf(
            userModule,
            productModule,
            orderModule
        )
    )
)
```
