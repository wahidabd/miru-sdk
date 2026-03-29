---
sidebar_position: 6
title: PagingState
---

# PagingState\<T\>

Data class for managing paginated list state in ViewModels.

**Module:** `:ui-state`

## Definition

```kotlin
data class PagingState<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 1,
    val isLoading: Boolean = false,
    val hasMore: Boolean = true,
    val error: String? = null
)
```

## Properties

| Property | Type | Description |
|---|---|---|
| `items` | `List<T>` | All loaded items |
| `currentPage` | `Int` | Current page number |
| `isLoading` | `Boolean` | Whether a page is being loaded |
| `hasMore` | `Boolean` | Whether more pages are available |
| `error` | `String?` | Error message if last load failed |

## Operations

### appendItems

Add items from a new page:

```kotlin
setState { copy(paging = paging.appendItems(newPosts)) }
```

### refresh

Replace all items (pull-to-refresh):

```kotlin
setState { copy(paging = paging.refresh(freshPosts)) }
```

## Example

```kotlin
data class FeedState(
    val paging: PagingState<Post> = PagingState()
)

class FeedViewModel(
    private val getPostsUseCase: GetPostsUseCase
) : BaseViewModel<FeedState, FeedEvent>(FeedState()) {

    fun loadNextPage() = execute(
        call = { getPostsUseCase(page = currentState.paging.currentPage) },
        onLoading = { copy(paging = paging.copy(isLoading = true)) },
        onSuccess = { posts ->
            copy(paging = paging.appendItems(posts))
        },
        onError = { copy(paging = paging.copy(isLoading = false, error = it.message)) }
    )

    fun refresh() = execute(
        call = { getPostsUseCase(page = 1) },
        onSuccess = { posts ->
            copy(paging = paging.refresh(posts))
        }
    )
}
```
