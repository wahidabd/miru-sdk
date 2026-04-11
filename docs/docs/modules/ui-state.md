---
sidebar_position: 3
title: UI State
---

# UI State Module

Presentation layer module providing `BaseViewModel` with MVI-style state management, one-time events, and 5 async helpers.

```kotlin
implementation("io.github.wahidabd:miru-sdk-ui-state:<version>")
```

## BaseViewModel

Abstract base class for all ViewModels. Manages typed state and one-time UI events:

```kotlin
class MyViewModel(
    private val useCase: MyUseCase
) : BaseViewModel<MyState, MyEvent>(MyState()) {
    // ...
}
```

### State Management

```kotlin
// Read current state
val current = currentState

// Update state with a reducer
setState { copy(isLoading = true) }

// Observe in Composable
val state by viewModel.uiState.collectAsStateWithLifecycle()
```

### Events

One-time events (navigation, snackbar, etc.) that should not survive recomposition:

```kotlin
// Emit event
sendEvent(MyEvent.NavigateToDetail(id))

// Collect in Composable
viewModel.events.collectAsEffect { event ->
    when (event) {
        is MyEvent.NavigateToDetail -> navController.navigate("detail/${event.id}")
        is MyEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
    }
}
```

## Async Helpers

BaseViewModel provides 5 helpers for handling async operations. Choose based on how much control you need:

### collectResource()

**Simplest** — pipes a one-shot `suspend -> Resource<T>` directly into a `MutableStateFlow`. Zero boilerplate.

```kotlin
private val _users = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
val users = _users.asStateFlow()

fun loadUsers() = collectResource(_users) { getUsersUseCase() }
```

Best for: screens where the entire UI is driven by a single Resource state. Pair with [MiruResourceView](/docs/api/miru-resource-view) for zero-boilerplate screens.

### collectFlow()

Pipes a plain `Flow<T>` into a `MutableStateFlow<Resource<T>>` — auto-wraps emissions in `Resource.Success`:

```kotlin
private val _notifications = MutableStateFlow<Resource<List<Notification>>>(Resource.Loading())
val notifications = _notifications.asStateFlow()

fun observe() = collectFlow(_notifications, distinctUntilChanged = true) {
    observeNotificationsUseCase() // returns Flow<List<Notification>>
}
```

Best for: observing Room queries or other plain Flow streams.

### collectFlowResource()

Pipes a `Flow<Resource<T>>` directly into a `MutableStateFlow<Resource<T>>`:

```kotlin
private val _feed = MutableStateFlow<Resource<List<FeedItem>>>(Resource.Loading())
val feed = _feed.asStateFlow()

fun observe() = collectFlowResource(_feed) {
    observeLiveFeedUseCase() // returns Flow<Resource<List<FeedItem>>>
}
```

Best for: streams that already emit `Resource` (e.g., network polling with loading/error states).

### execute()

One-shot suspend call with **state reducers** — gives you full control over how loading, success, and error map to your state:

```kotlin
fun loadProducts() = execute(
    call = { getProductsUseCase() },
    onLoading = { copy(isLoading = true, error = null) },
    onSuccess = { copy(products = it, isLoading = false) },
    onError = { copy(isLoading = false, error = it.message) },
    errorEvent = { MyEvent.ShowError(it.message ?: "Unknown error") }
)
```

Best for: screens with complex state (multiple fields, loading indicators, error messages).

### collect()

Reactive stream (`Flow<Resource<T>>`) with **state reducers**:

```kotlin
fun observeBookmarks() = collect(
    flow = { observeBookmarksUseCase() },
    distinctUntilChanged = true,
    onLoading = { copy(isLoading = true) },
    onSuccess = { copy(bookmarks = it, isLoading = false) },
    onError = { copy(isLoading = false, error = it.message) }
)
```

Best for: same as `execute()` but for reactive streams instead of one-shot calls.

## Choosing the Right Helper

| Helper | Input | Output | Control Level |
|---|---|---|---|
| `collectResource` | `suspend -> Resource<T>` | `MutableStateFlow<Resource<T>>` | Minimal |
| `collectFlow` | `Flow<T>` | `MutableStateFlow<Resource<T>>` | Minimal |
| `collectFlowResource` | `Flow<Resource<T>>` | `MutableStateFlow<Resource<T>>` | Minimal |
| `execute` | `suspend -> Resource<T>` | State reducers | Full |
| `collect` | `Flow<Resource<T>>` | State reducers | Full |

:::tip
See [ViewModel Patterns Guide](/docs/guides/viewmodel-patterns) for real-world examples showing all 5 helpers in one ViewModel.
:::

## PagingState

Built-in pagination state management:

```kotlin
data class FeedState(
    val paging: PagingState<Post> = PagingState()
)

// Append new page
setState { copy(paging = paging.appendItems(newPosts)) }

// Refresh
setState { copy(paging = paging.refresh(freshPosts)) }

// Check state
paging.isLoading
paging.hasMore
paging.items
paging.currentPage
```

## EventFlow

Channel-backed event flow for one-time UI events:

```kotlin
val events = MutableEventFlow<MyEvent>()

// Send
events.send(MyEvent.ShowToast("Done!"))

// Collect (in Composable)
events.collectAsEffect { event -> /* handle */ }
```
