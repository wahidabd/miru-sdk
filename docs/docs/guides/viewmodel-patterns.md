---
sidebar_position: 1
title: ViewModel Patterns
---

# ViewModel Patterns

This guide shows all 5 BaseViewModel helpers in action, explaining when to use each one.

## The Two Approaches

BaseViewModel supports two fundamentally different patterns:

**Pipe approach** (`collectResource`, `collectFlow`, `collectFlowResource`) — each data source gets its own `MutableStateFlow<Resource<T>>`. The UI collects each flow independently and renders via `MiruResourceView`. Minimal boilerplate, no state reducers.

**Reducer approach** (`execute`, `collect`) — all data merges into a single `State` data class via reducer functions. More control, but more code.

You can mix both in the same ViewModel.

## Pattern 1: collectResource

Best for one-shot suspend calls where a single `Resource` drives the UI.

```kotlin
class SpotlightViewModel(
    private val useCase: GetSpotlightUseCase
) : BaseViewModel<Unit, SpotlightEvent>(Unit) {

    private val _spotlight = MutableStateFlow<Resource<List<Spotlight>>>(Resource.Loading())
    val spotlight = _spotlight.asStateFlow()

    fun load() = collectResource(_spotlight) { useCase.spotlight() }
}
```

**Screen:**

```kotlin
@Composable
fun SpotlightScreen(viewModel: SpotlightViewModel = koinViewModel()) {
    val resource by viewModel.spotlight.collectAsStateWithLifecycle()

    MiruResourceView(resource = resource, onRetry = { viewModel.load() }) { items ->
        LazyColumn {
            items(items) { SpotlightCard(it) }
        }
    }
}
```

## Pattern 2: collectFlow

Best for observing Room queries or other plain `Flow<T>` streams.

```kotlin
class BookmarkViewModel(
    private val observeBookmarks: ObserveBookmarksUseCase
) : BaseViewModel<Unit, BookmarkEvent>(Unit) {

    private val _bookmarks = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
    val bookmarks = _bookmarks.asStateFlow()

    fun observe() = collectFlow(_bookmarks, distinctUntilChanged = true) {
        observeBookmarks() // returns Flow<List<Article>>
    }
}
```

The `Flow<List<Article>>` emissions are auto-wrapped in `Resource.Success`. If the flow throws, it becomes `Resource.Error`.

## Pattern 3: collectFlowResource

Best for streams that already emit `Resource<T>` (e.g., a use case that handles loading/error internally).

```kotlin
class LiveFeedViewModel(
    private val observeLiveFeed: ObserveLiveFeedUseCase
) : BaseViewModel<Unit, LiveFeedEvent>(Unit) {

    private val _feed = MutableStateFlow<Resource<List<FeedItem>>>(Resource.Loading())
    val feed = _feed.asStateFlow()

    fun observe() = collectFlowResource(_feed) {
        observeLiveFeed() // returns Flow<Resource<List<FeedItem>>>
    }
}
```

## Pattern 4: execute

Best for one-shot calls where you need to update multiple state fields.

```kotlin
data class ProductState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProductViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel<ProductState, ProductEvent>(ProductState()) {

    fun loadProducts() = execute(
        call = { getProductsUseCase() },
        onLoading = { copy(isLoading = true, error = null) },
        onSuccess = { copy(products = it, isLoading = false) },
        onError = { copy(isLoading = false, error = it.message) },
        errorEvent = { ProductEvent.ShowError(it.message ?: "Failed") }
    )
}
```

## Pattern 5: collect

Best for reactive streams where you need state reducers.

```kotlin
class DashboardViewModel(
    private val observeStats: ObserveStatsUseCase
) : BaseViewModel<DashboardState, DashboardEvent>(DashboardState()) {

    fun observeStats() = collect(
        flow = { observeStats() },
        distinctUntilChanged = true,
        onLoading = { copy(isLoading = true) },
        onSuccess = { copy(stats = it, isLoading = false) },
        onError = { copy(isLoading = false, error = it.message) }
    )
}
```

## Mixing Patterns in One ViewModel

A real-world ViewModel often combines multiple patterns:

```kotlin
class HomeViewModel(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<HomeState, HomeEvent>(HomeState()) {

    // Pipe pattern — each has its own Resource flow
    private val _articles = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
    val articles = _articles.asStateFlow()

    private val _bookmarks = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
    val bookmarks = _bookmarks.asStateFlow()

    // collectResource — one-shot suspend
    fun loadArticles() = collectResource(_articles) {
        getArticlesUseCase(currentState.selectedCategory)
    }

    // collectFlow — plain Flow<T>
    private fun observeBookmarks() = collectFlow(_bookmarks, distinctUntilChanged = true) {
        getBookmarksUseCase()
    }

    // collect — stream with state reducer (for derived state)
    private fun observeBookmarkCount() = collect(
        flow = { getBookmarksUseCase().asResource() },
        onSuccess = { copy(bookmarkCount = it.size) }
    )

    // execute — one-shot action with state reducer
    fun toggleBookmark(article: Article) = execute(
        call = { Resource.Success(toggleBookmarkUseCase(article)) },
        onSuccess = { this },
        errorEvent = { HomeEvent.ShowError(it.message ?: "Failed") }
    )
}
```

## Decision Guide

| Scenario | Helper | Why |
|---|---|---|
| Simple list from API | `collectResource` | One Resource drives the entire screen |
| Observe Room database | `collectFlow` | Room returns `Flow<T>`, not `Flow<Resource>` |
| Real-time stream with status | `collectFlowResource` | Stream already handles loading/error |
| Form submission, login | `execute` | Need to update isLoading, error, success fields |
| Live dashboard counters | `collect` | Stream + need to merge into composite state |
| Mix of the above | Combine freely | Use pipes for independent data, reducers for derived state |
