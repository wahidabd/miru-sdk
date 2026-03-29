package com.miru.sdk.ui.state

import com.miru.sdk.core.AppException

/**
 * Represents the state of a paginated list of items.
 *
 * @param T The type of items in the paginated list
 * @param items The current list of items
 * @param currentPage The current page number (1-indexed)
 * @param isLoadingMore Whether more items are currently being loaded
 * @param isRefreshing Whether the list is being refreshed
 * @param hasMore Whether there are more pages available
 * @param error The last error that occurred, if any
 */
data class PagingState<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 1,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasMore: Boolean = true,
    val error: AppException? = null
)

/**
 * Checks if the paginated list is empty
 */
fun <T> PagingState<T>.isEmpty(): Boolean = items.isEmpty()

/**
 * Checks if the list is in its first loading state (no items yet and loading)
 */
fun <T> PagingState<T>.isFirstLoading(): Boolean = items.isEmpty() && isLoadingMore && currentPage == 1

/**
 * Appends new items to the list and increments the page number
 *
 * @param newItems The items to append
 * @return A new [PagingState] with appended items
 */
fun <T> PagingState<T>.appendItems(newItems: List<T>): PagingState<T> = copy(
    items = items + newItems,
    currentPage = currentPage + 1,
    isLoadingMore = false,
    error = null
)

/**
 * Resets the state for refresh operation
 *
 * @param newItems The items to set after refresh (typically first page items)
 * @return A new [PagingState] with refreshed items
 */
fun <T> PagingState<T>.refresh(newItems: List<T>): PagingState<T> = copy(
    items = newItems,
    currentPage = 1,
    isRefreshing = false,
    error = null,
    hasMore = true
)

/**
 * Sets the error state when loading more items fails
 *
 * @param exception The exception that occurred
 * @return A new [PagingState] with error information
 */
fun <T> PagingState<T>.loadMoreError(exception: AppException): PagingState<T> = copy(
    isLoadingMore = false,
    error = exception
)
