---
sidebar_position: 2
title: BaseViewModel
---

# BaseViewModel\<State, Event\>

Abstract ViewModel base class providing typed state management, one-time events, and 5 async helpers.

**Module:** `:ui-state`

## Definition

```kotlin
abstract class BaseViewModel<State, Event>(
    initialState: State
) : ViewModel()
```

## Properties

| Property | Type | Description |
|---|---|---|
| `uiState` | `StateFlow<State>` | Observable UI state |
| `currentState` | `State` | Current state snapshot |
| `events` | `MutableEventFlow<Event>` | One-time UI events |

## State Management

### setState

Update state with a reducer function:

```kotlin
setState { copy(isLoading = true, error = null) }
```

The lambda receiver is the current state — use `copy()` for data classes.

### sendEvent

Emit a one-time UI event:

```kotlin
sendEvent(MyEvent.ShowSnackbar("Item deleted"))
sendEvent(MyEvent.NavigateToDetail(itemId))
```

## Async Helpers

### collectResource

Pipes a one-shot `suspend -> Resource<T>` into a `MutableStateFlow<Resource<T>>`.

```kotlin
protected fun <T> collectResource(
    stateFlow: MutableStateFlow<Resource<T>>,
    call: suspend () -> Resource<T>
): Job
```

**Example:**

```kotlin
private val _users = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
val users = _users.asStateFlow()

fun loadUsers() = collectResource(_users) { getUsersUseCase() }
```

---

### collectFlow

Pipes a plain `Flow<T>` into a `MutableStateFlow<Resource<T>>`. Each emission is auto-wrapped in `Resource.Success`.

```kotlin
protected fun <T> collectFlow(
    stateFlow: MutableStateFlow<Resource<T>>,
    distinctUntilChanged: Boolean = false,
    flow: suspend () -> Flow<T>
): Job
```

**Example:**

```kotlin
private val _bookmarks = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
val bookmarks = _bookmarks.asStateFlow()

fun observe() = collectFlow(_bookmarks, distinctUntilChanged = true) {
    bookmarkDao.getAll() // Flow<List<Article>>
}
```

---

### collectFlowResource

Pipes a `Flow<Resource<T>>` directly into a `MutableStateFlow<Resource<T>>`.

```kotlin
protected fun <T> collectFlowResource(
    stateFlow: MutableStateFlow<Resource<T>>,
    distinctUntilChanged: Boolean = false,
    flow: suspend () -> Flow<Resource<T>>
): Job
```

**Example:**

```kotlin
private val _feed = MutableStateFlow<Resource<List<FeedItem>>>(Resource.Loading())
val feed = _feed.asStateFlow()

fun observe() = collectFlowResource(_feed) {
    liveFeedUseCase() // Flow<Resource<List<FeedItem>>>
}
```

---

### execute

One-shot suspend call with state reducers for full control:

```kotlin
protected fun <T> execute(
    call: suspend () -> Resource<T>,
    onLoading: (State.() -> State)? = null,
    onSuccess: State.(T) -> State,
    onError: (State.(AppException) -> State)? = null,
    errorEvent: ((AppException) -> Event)? = null
): Job
```

**Parameters:**

| Parameter | Type | Description |
|---|---|---|
| `call` | `suspend () -> Resource<T>` | The async operation |
| `onLoading` | `(State.() -> State)?` | State reducer for loading state |
| `onSuccess` | `State.(T) -> State` | State reducer for success (required) |
| `onError` | `(State.(AppException) -> State)?` | State reducer for error |
| `errorEvent` | `((AppException) -> Event)?` | Emit an event on error |

**Example:**

```kotlin
fun loadProducts() = execute(
    call = { getProductsUseCase() },
    onLoading = { copy(isLoading = true) },
    onSuccess = { copy(products = it, isLoading = false) },
    onError = { copy(error = it.message, isLoading = false) },
    errorEvent = { ProductEvent.ShowError(it.message ?: "Failed") }
)
```

---

### collect

Reactive stream with state reducers:

```kotlin
protected fun <T> collect(
    flow: () -> Flow<Resource<T>>,
    distinctUntilChanged: Boolean = false,
    onLoading: (State.() -> State)? = null,
    onSuccess: State.(T) -> State,
    onError: (State.(AppException) -> State)? = null,
    errorEvent: ((AppException) -> Event)? = null
): Job
```

**Example:**

```kotlin
fun observeBookmarks() = collect(
    flow = { observeBookmarksUseCase() },
    distinctUntilChanged = true,
    onSuccess = { copy(bookmarks = it, isLoading = false) }
)
```

## Decision Matrix

| Need | Use |
|---|---|
| Simplest possible — one Resource drives the UI | `collectResource` |
| Observe a Room/DataStore Flow | `collectFlow` |
| Observe a Flow that already emits Resource | `collectFlowResource` |
| One-shot call, need to update multiple state fields | `execute` |
| Stream, need to update multiple state fields | `collect` |
