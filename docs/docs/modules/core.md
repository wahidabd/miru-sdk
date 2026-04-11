---
sidebar_position: 1
title: Core
---

# Core Module

The foundation layer with zero UI dependencies. Every other module in Miru SDK depends on `:core`.

```kotlin
implementation("io.github.wahidabd:miru-sdk-core:<version>")
```

## Resource

`Resource<T>` is a sealed class that wraps all async operations into three states:

```kotlin
sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val exception: AppException, val data: T? = null) : Resource<T>()
    class Loading<T>(val data: T? = null) : Resource<T>()
}
```

### Callbacks

```kotlin
val result: Resource<User> = userApi.getUserById(1)

result
    .onSuccess { user -> println(user.name) }
    .onError { exception, data -> println(exception.message) }
    .onLoading { println("Loading...") }
```

### Transformations

```kotlin
// Transform the success data
val mapped: Resource<String> = result.map { it.name }

// Chain operations
val chained: Resource<Profile> = result.flatMap { user ->
    profileApi.getProfile(user.id)
}
```

### Data Retrieval

```kotlin
val user: User? = result.getOrNull()
val userSafe: User = result.getOrDefault(User.empty())
val userForced: User = result.getOrThrow()
```

### Flow Conversion

```kotlin
// Convert Flow<T> to Flow<Resource<T>>
val resourceFlow: Flow<Resource<List<User>>> = userFlow.asResource()
```

:::info
See [Resource API Reference](/docs/api/resource) for the complete API.
:::

## AppException

Typed exception hierarchy for structured error handling across layers:

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

Pattern match in your ViewModel or global error handler:

```kotlin
when (exception) {
    is AppException.UnauthorizedException -> navigateToLogin()
    is AppException.NetworkException -> showOfflineMessage()
    is AppException.ServerException -> showServerError(exception.code)
    is AppException.TimeoutException -> showRetryDialog()
    else -> showGenericError()
}
```

:::tip
See [Error Handling Guide](/docs/guides/error-handling) for best practices.
:::

## Mapper

Type-safe mapping interface for DTO-to-domain conversions:

```kotlin
class UserMapper : Mapper<UserDto, User> {
    override fun map(from: UserDto) = User(
        id = from.id,
        name = from.name,
        email = from.email
    )
}
```

## Extensions

### String Extensions

```kotlin
"hello world".capitalizeFirst()   // "Hello world"
"test@email.com".isValidEmail()   // true
```

### Flow Extensions

```kotlin
flow.throttleFirst(300L)
flow.retryWithExponentialBackoff(maxRetries = 3)
flow.asResource() // Flow<T> → Flow<Resource<T>>
```

### Collection Extensions

```kotlin
list.safeGet(99) // null instead of IndexOutOfBoundsException
list.updateIf({ it.id == 5 }) { it.copy(name = "Updated") }
```

### DateTime Extensions

Kotlinx DateTime helpers for common date/time operations.

## Logger

Napier-based multiplatform logging:

```kotlin
MiruLogger.d("Debug message")
MiruLogger.e("Error message", throwable)
```
