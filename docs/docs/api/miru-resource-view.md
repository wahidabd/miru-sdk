---
sidebar_position: 3
title: MiruResourceView
---

# MiruResourceView

Composable that eliminates repetitive `when (resource)` boilerplate by declaratively rendering loading, error, and success states.

**Module:** `:ui-components`

## Definition

```kotlin
@Composable
fun <T> MiruResourceView(
    resource: Resource<T>,
    modifier: Modifier = Modifier,
    loadingMessage: String? = null,
    onRetry: (() -> Unit)? = null,
    onLoading: (@Composable () -> Unit)? = null,
    onError: (@Composable (String) -> Unit)? = null,
    content: @Composable (T) -> Unit
)
```

## Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `resource` | `Resource<T>` | required | The resource state to render |
| `modifier` | `Modifier` | `Modifier` | Applied to the root container |
| `loadingMessage` | `String?` | `null` | Message displayed in the default loading view |
| `onRetry` | `(() -> Unit)?` | `null` | Retry callback shown in the default error view |
| `onLoading` | `@Composable?` | `null` | Custom loading composable (overrides default) |
| `onError` | `@Composable (String)?` | `null` | Custom error composable (overrides default) |
| `content` | `@Composable (T)` | required | Content rendered when resource is `Success` |

## Default Behavior

| State | Default Rendering |
|---|---|
| `Resource.Loading` | `MiruFullScreenLoading(message = loadingMessage)` |
| `Resource.Error` | `MiruErrorView(message = ..., onRetry = onRetry)` |
| `Resource.Success` | Calls `content(data)` |

## Basic Usage

```kotlin
@Composable
fun UserListScreen(viewModel: UserListViewModel = koinViewModel()) {
    val usersResource by viewModel.users.collectAsStateWithLifecycle()

    MiruResourceView(
        resource = usersResource,
        loadingMessage = "Loading users...",
        onRetry = { viewModel.loadUsers() }
    ) { users ->
        LazyColumn {
            items(users, key = { it.id }) { user ->
                UserCard(user)
            }
        }
    }
}
```

## Custom Loading & Error

```kotlin
MiruResourceView(
    resource = feedResource,
    onRetry = { viewModel.refresh() },
    onLoading = {
        // Custom shimmer placeholder
        Column {
            repeat(5) { ShimmerCard() }
        }
    },
    onError = { message ->
        // Custom error banner
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, contentDescription = null)
            Text(message)
            MiruButton(text = "Try Again", onClick = { viewModel.refresh() })
        }
    }
) { data ->
    FeedContent(data)
}
```

## Data Source Agnostic

`MiruResourceView` works with any `StateFlow<Resource<T>>`, regardless of which BaseViewModel helper populated it:

```kotlin
// collectResource — suspend one-shot
fun load() = collectResource(_data) { useCase() }

// collectFlow — plain Flow<T>
fun observe() = collectFlow(_data) { dao.getAll() }

// collectFlowResource — Flow<Resource<T>>
fun stream() = collectFlowResource(_data) { useCase.observe() }
```

All three produce a `StateFlow<Resource<T>>` that you collect and pass to `MiruResourceView`:

```kotlin
val dataResource by viewModel.data.collectAsStateWithLifecycle()
MiruResourceView(resource = dataResource) { data -> /* render */ }
```

## Before vs After

**Before** (manual handling):

```kotlin
val resource by viewModel.users.collectAsStateWithLifecycle()

when (resource) {
    is Resource.Loading -> MiruFullScreenLoading()
    is Resource.Error -> MiruErrorView(
        message = (resource as Resource.Error).exception.message ?: "Error",
        onRetry = { viewModel.loadUsers() }
    )
    is Resource.Success -> {
        val users = (resource as Resource.Success).data
        LazyColumn {
            items(users) { UserCard(it) }
        }
    }
}
```

**After** (with MiruResourceView):

```kotlin
val resource by viewModel.users.collectAsStateWithLifecycle()

MiruResourceView(
    resource = resource,
    onRetry = { viewModel.loadUsers() }
) { users ->
    LazyColumn {
        items(users) { UserCard(it) }
    }
}
```
