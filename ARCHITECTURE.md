# Miru SDK Architecture

## Overview

Miru SDK is a Kotlin Multiplatform internal library designed to accelerate development for Android and iOS projects using Compose Multiplatform. It provides a modular set of tools covering networking, state management, navigation, UI components, and dependency injection.

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Kotlin | 2.3.0 | Language |
| Compose Multiplatform | 1.10.0 | Shared UI framework |
| Ktor | 3.4.1 | HTTP client |
| Koin | 4.0.3 | Dependency injection |
| Kotlinx Serialization | 1.7.3 | JSON parsing |
| Kotlinx Coroutines | 1.9.0 | Async programming |
| Kotlinx DateTime | 0.6.1 | Date/time utilities |
| Coil 3 | 3.0.4 | Image loading |
| Napier | 2.7.1 | Multiplatform logging |

## Module Structure

```
miru-sdk/
├── core/           → Base utilities, Resource wrapper, extensions, logging, mappers
├── network/        → Ktor HTTP client, API service base, safe API calls, token management
├── ui-state/       → BaseViewModel, UiState, events, pagination state
├── navigation/     → Navigation manager, transitions, result passing
├── ui-components/  → Reusable Compose components, theme system
└── di/             → Koin DI setup, SDK initializer
```

## Module Dependencies

```
di ──────→ network ──→ core
  └──────────────────→ core
ui-state ────────────→ core
navigation ──────────→ core
ui-components ───────→ core
```

## Module Details

### :core
Base module with zero external UI dependencies. Contains:
- `Resource<T>` — sealed class (Success/Error/Loading) for async state
- `AppException` — sealed exception hierarchy (Network, API, Unauthorized, etc.)
- `DispatcherProvider` — expect/actual for coroutine dispatchers
- `MiruLogger` — logging wrapper using Napier
- `Mapper<From, To>` — base mapper interfaces
- Extension functions for String, Flow, Collection, DateTime

### :network
HTTP networking layer built on Ktor:
- `HttpClientFactory` — configures Ktor with JSON, logging, timeouts
- `ApiService` — abstract base with `get/post/put/patch/delete` helpers
- `safeApiCall()` — wraps API calls in Resource with exception mapping
- `TokenProvider` — interface for auth token management
- `TokenEventBus` — SharedFlow for token lifecycle events
- `ApiResponse<T>` / `PaginationMeta` — standard response models
- Platform engines: OkHttp (Android), Darwin (iOS)

### :ui-state
State management for Compose UI:
- `BaseViewModel<State, Event>` — base ViewModel with setState/sendEvent
- `UiState<T>` — sealed interface (Idle/Loading/Success/Error)
- `MutableEventFlow<T>` — channel-based one-time events
- `PagingState<T>` — pagination state helper
- `collectAsEffect()` — composable for consuming events

### :navigation
Navigation abstraction for Compose Multiplatform:
- `NavigationManager` — command-based navigation via Channel
- `MiruNavHost` — composable NavHost wrapper
- Safe navigation extensions preventing duplicate navigation
- Predefined transition animations
- Type-safe result passing between screens

### :ui-components
Reusable Compose Multiplatform components:
- `MiruTheme` — theme system with colors, typography, shapes
- `MiruButton` — buttons with Primary/Secondary/Outline/Text/Danger styles
- `MiruTextField` / `MiruPasswordField` / `MiruSearchField`
- `MiruDialog` / `MiruLoadingDialog` / `MiruConfirmationDialog`
- `MiruLoadingIndicator` / `MiruFullScreenLoading` / `MiruShimmerEffect`
- `MiruErrorView` / `MiruEmptyView`
- `MiruNetworkImage` — async image loading with Coil
- `MiruTopBar` / `MiruSearchTopBar`
- `MiruBottomSheet` / `MiruCard` / spacer utilities

### :di
Dependency injection and SDK initialization:
- `MiruSdkInitializer` — single entry point for SDK setup
- `MiruSdkConfig` — configuration data class
- Koin modules for core, network, and platform dependencies
- Composable injection helpers (`koinInject`, `koinViewModel`)

## Quick Start

### 1. Initialize SDK

```kotlin
// In your Application class or app entry point
MiruSdkInitializer.initialize(
    MiruSdkConfig(
        networkConfig = NetworkConfig(
            baseUrl = "https://api.example.com/v1/",
            enableLogging = BuildConfig.DEBUG
        ),
        enableLogging = true,
        tokenProvider = MyTokenProvider() // optional
    )
)
```

### 2. Create an API Service

```kotlin
class UserApiService(httpClient: HttpClient) : ApiService(httpClient) {
    suspend fun getUsers(): Resource<ApiResponse<List<User>>> = get("users")
    suspend fun getUser(id: String): Resource<ApiResponse<User>> = get("users/$id")
    suspend fun createUser(request: CreateUserRequest): Resource<ApiResponse<User>> = post("users", body = request)
}
```

### 3. Create a ViewModel

```kotlin
class UserListViewModel(
    private val userApi: UserApiService
) : BaseViewModel<UserListState, UserListEvent>(UserListState()) {

    fun loadUsers() = launch {
        setState { copy(isLoading = true) }
        userApi.getUsers()
            .onSuccess { response ->
                setState { copy(users = response.data ?: emptyList(), isLoading = false) }
            }
            .onError { exception ->
                setState { copy(isLoading = false, error = exception.message) }
                sendEvent(UserListEvent.ShowError(exception.message))
            }
    }
}

data class UserListState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface UserListEvent {
    data class ShowError(val message: String) : UserListEvent
}
```

### 4. Build the UI

```kotlin
@Composable
fun UserListScreen(viewModel: UserListViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateLifecycleAware()

    viewModel.events.collectAsEffect { event ->
        when (event) {
            is UserListEvent.ShowError -> { /* show snackbar */ }
        }
    }

    MiruTheme {
        when {
            state.isLoading -> MiruFullScreenLoading()
            state.error != null -> MiruErrorView(
                message = state.error!!,
                onRetry = { viewModel.loadUsers() }
            )
            else -> LazyColumn {
                items(state.users) { user ->
                    MiruInfoCard(title = user.name, subtitle = user.email)
                }
            }
        }
    }
}
```

## Platform Targets

| Platform | Target | Engine |
|---|---|---|
| Android | androidTarget (minSdk 24) | OkHttp |
| iOS | iosX64, iosArm64, iosSimulatorArm64 | Darwin |

## Build

```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew :core:build
./gradlew :network:build
```
