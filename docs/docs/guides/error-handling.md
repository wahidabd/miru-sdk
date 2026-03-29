---
sidebar_position: 3
title: Error Handling
---

# Error Handling Guide

Miru SDK provides a structured error handling pipeline from network layer through to UI.

## The Pipeline

```
HTTP Response → safeApiCall → AppException → Resource.Error → ViewModel → UI
```

1. **safeApiCall** catches Ktor exceptions and HTTP error codes
2. Maps them to typed **AppException** subclasses
3. Wraps in **Resource.Error** with the exception
4. ViewModel receives it via helpers (`execute`, `collect`, etc.)
5. UI renders the error state

## Network Layer (Automatic)

When you extend `ApiService`, all HTTP calls are automatically wrapped:

```kotlin
class UserApi(httpClient: HttpClient) : ApiService(httpClient) {
    suspend fun getUsers(): Resource<ApiResponse<List<UserDto>>> = get("users")
}
```

If the request fails, `safeApiCall` maps it:

| Scenario | AppException |
|---|---|
| HTTP 401 | `UnauthorizedException` |
| HTTP 403 | `ForbiddenException` |
| HTTP 404 | `NotFoundException` |
| HTTP 500-599 | `ServerException(code)` |
| Connection timeout | `TimeoutException` |
| No internet | `NetworkException` |
| Anything else | `UnknownException(cause)` |

## Repository Layer

Repositories receive `Resource<T>` from the API and can transform it:

```kotlin
class UserRepositoryImpl(
    private val api: UserApi,
    private val mapper: UserMapper
) : UserRepository {

    override suspend fun getUsers(): Resource<List<User>> =
        api.getUsers().map { response ->
            response.data?.map { mapper.map(it) } ?: emptyList()
        }
}
```

If the API returned `Resource.Error`, `.map()` passes it through untouched.

## ViewModel Layer

### With execute / collect (State Reducers)

Handle errors via the `onError` reducer and optional `errorEvent`:

```kotlin
fun loadProducts() = execute(
    call = { getProductsUseCase() },
    onSuccess = { copy(products = it, isLoading = false) },
    onError = { exception ->
        when (exception) {
            is AppException.UnauthorizedException -> {
                // Could trigger navigation
                copy(needsLogin = true)
            }
            is AppException.NetworkException -> {
                copy(isOffline = true, isLoading = false)
            }
            else -> {
                copy(error = exception.message, isLoading = false)
            }
        }
    },
    errorEvent = { ProductEvent.ShowSnackbar(it.message ?: "Error") }
)
```

### With collectResource / collectFlow / collectFlowResource (Pipe Pattern)

Errors are captured in `Resource.Error` and rendered by `MiruResourceView`:

```kotlin
fun load() = collectResource(_products) { getProductsUseCase() }
```

```kotlin
MiruResourceView(
    resource = productsResource,
    onRetry = { viewModel.load() }
) { products ->
    ProductList(products)
}
```

`MiruResourceView` shows `MiruErrorView` with the error message and optional retry button.

## UI Layer

### MiruResourceView (Recommended)

Handles loading/error/success automatically:

```kotlin
MiruResourceView(
    resource = resource,
    onRetry = { viewModel.reload() },
    onError = { message ->
        // Optional: custom error UI
        MyErrorBanner(message)
    }
) { data ->
    Content(data)
}
```

### Manual Handling

For the reducer pattern where errors are in the state:

```kotlin
val state by viewModel.uiState.collectAsStateWithLifecycle()

when {
    state.needsLogin -> LoginPrompt()
    state.isOffline -> OfflineBanner(onRetry = { viewModel.retry() })
    state.error != null -> MiruErrorView(message = state.error!!, onRetry = { viewModel.retry() })
    else -> Content(state.data)
}
```

### Events for Transient Errors

Use events for errors that should show once (snackbar, toast):

```kotlin
viewModel.events.collectAsEffect { event ->
    when (event) {
        is ProductEvent.ShowSnackbar -> {
            snackbarHostState.showSnackbar(event.message)
        }
    }
}
```

## Global Error Handling

### Token Expiry

Use `TokenEventBus` for global auth state:

```kotlin
// In your root composable or Application class
TokenEventBus.events.collect { event ->
    when (event) {
        TokenEvent.ForceLogout -> {
            authManager.signOut()
            navigateToLogin()
        }
        TokenEvent.TokenExpired -> refreshToken()
        TokenEvent.TokenRefreshed -> { /* retry pending requests */ }
    }
}
```

### Custom Error Mapping

If your API uses custom error codes, map them in your repository:

```kotlin
override suspend fun getUsers(): Resource<List<User>> {
    val result = api.getUsers()
    return when {
        result is Resource.Error &&
        result.exception is AppException.ServerException &&
        (result.exception as AppException.ServerException).code == 503 ->
            Resource.Error(AppException.NetworkException("Service temporarily unavailable"))
        else -> result.map { it.data?.map { mapper.map(it) } ?: emptyList() }
    }
}
```
