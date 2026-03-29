---
sidebar_position: 1
title: Resource<T>
---

# Resource\<T\>

Sealed class wrapping async operation results into three states: `Success`, `Error`, and `Loading`.

**Module:** `:core`

## Definition

```kotlin
sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(
        val exception: AppException,
        val data: T? = null
    ) : Resource<T>()
    class Loading<T>(val data: T? = null) : Resource<T>()
}
```

## State Checks

```kotlin
resource.isSuccess()  // Boolean
resource.isError()    // Boolean
resource.isLoading()  // Boolean
```

## Callbacks

Chain callbacks for each state:

```kotlin
resource
    .onSuccess { data -> /* handle success */ }
    .onError { exception, partialData -> /* handle error */ }
    .onLoading { partialData -> /* handle loading */ }
```

Each callback returns `this`, so they can be chained.

## Transformations

### map

Transform the success data:

```kotlin
val userResource: Resource<User> = api.getUser(1)
val nameResource: Resource<String> = userResource.map { it.name }
```

### flatMap

Chain dependent async operations:

```kotlin
val profileResource: Resource<Profile> = userResource.flatMap { user ->
    profileApi.getProfile(user.id)
}
```

## Data Retrieval

| Method | Return | Behavior |
|---|---|---|
| `getOrNull()` | `T?` | Returns data on success, null otherwise |
| `getOrDefault(default)` | `T` | Returns data on success, default otherwise |
| `getOrThrow()` | `T` | Returns data on success, throws on error/loading |

## Flow Extension

Convert a plain `Flow<T>` into `Flow<Resource<T>>`:

```kotlin
val flow: Flow<List<User>> = userDao.getAll()
val resourceFlow: Flow<Resource<List<User>>> = flow.asResource()
```

This emits `Resource.Loading()` first, then `Resource.Success(data)` for each emission, and `Resource.Error(exception)` on failure.

## Usage with BaseViewModel

`Resource<T>` is the fundamental return type used across the SDK:

```kotlin
// Repository returns Resource
interface UserRepository {
    suspend fun getUsers(): Resource<List<User>>
}

// ViewModel consumes Resource
fun loadUsers() = collectResource(_users) { repository.getUsers() }

// UI renders Resource
MiruResourceView(resource = usersResource) { users ->
    UserList(users)
}
```
