---
sidebar_position: 4
title: AppException
---

# AppException

Sealed exception hierarchy providing typed error handling across all layers of the application.

**Module:** `:core`

## Definition

```kotlin
sealed class AppException(message: String?) : Exception(message) {
    class NetworkException(message: String?) : AppException(message)
    class TimeoutException(message: String?) : AppException(message)
    class UnauthorizedException(message: String?) : AppException(message)
    class ForbiddenException(message: String?) : AppException(message)
    class NotFoundException(message: String?) : AppException(message)
    class ServerException(val code: Int, message: String?) : AppException(message)
    class UnknownException(cause: Throwable?) : AppException(cause?.message)
}
```

## Exception Types

| Type | HTTP Status | When |
|---|---|---|
| `NetworkException` | — | No internet / connection refused |
| `TimeoutException` | — | Request timed out |
| `UnauthorizedException` | 401 | Invalid or expired token |
| `ForbiddenException` | 403 | Insufficient permissions |
| `NotFoundException` | 404 | Resource not found |
| `ServerException` | 5xx | Server error (code is preserved) |
| `UnknownException` | — | Catch-all for unexpected errors |

## Pattern Matching

```kotlin
when (exception) {
    is AppException.UnauthorizedException -> {
        // Token expired — navigate to login
        navigateToLogin()
    }
    is AppException.NetworkException -> {
        // No connectivity
        showOfflineMessage()
    }
    is AppException.ServerException -> {
        // Server error with status code
        showServerError("Server error: ${exception.code}")
    }
    is AppException.TimeoutException -> {
        showRetryDialog()
    }
    is AppException.NotFoundException -> {
        showNotFound()
    }
    is AppException.ForbiddenException -> {
        showPermissionDenied()
    }
    is AppException.UnknownException -> {
        showGenericError(exception.message)
    }
}
```

## Usage with BaseViewModel

In `execute()` and `collect()`, the `onError` reducer receives `AppException`:

```kotlin
fun loadProducts() = execute(
    call = { getProductsUseCase() },
    onSuccess = { copy(products = it) },
    onError = { exception ->
        when (exception) {
            is AppException.UnauthorizedException -> copy(needsLogin = true)
            else -> copy(error = exception.message)
        }
    },
    errorEvent = { exception ->
        ProductEvent.ShowError(exception.message ?: "Something went wrong")
    }
)
```

## Automatic Mapping

The `:network` module's `safeApiCall` automatically maps HTTP responses and Ktor exceptions to `AppException`. You never need to create these exceptions manually when using `ApiService`.
