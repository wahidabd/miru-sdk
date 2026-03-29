package com.miru.sdk.sample.presentation.home

import com.miru.sdk.core.Resource
import com.miru.sdk.core.asResource
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.model.ArticleCategory
import com.miru.sdk.sample.domain.usecase.GetArticlesUseCase
import com.miru.sdk.sample.domain.usecase.GetBookmarksUseCase
import com.miru.sdk.sample.domain.usecase.ToggleBookmarkUseCase
import com.miru.sdk.ui.state.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeState(
    val selectedCategory: ArticleCategory = ArticleCategory.GENERAL,
    val bookmarkCount: Int = 0
)

sealed interface HomeEvent {
    data class ShowError(val message: String) : HomeEvent
    data class NavigateToDetail(val articleId: Long) : HomeEvent
    data class BookmarkToggled(val isBookmarked: Boolean) : HomeEvent
}

class HomeViewModel(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<HomeState, HomeEvent>(HomeState()) {

    // ─── collectResource() with suspend call ─────────────────────────
    // Simplest: one-shot suspend → pipe result into StateFlow. No Flow needed.
    private val _articles = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
    val articles: StateFlow<Resource<List<Article>>> = _articles.asStateFlow()

    // ─── collectResource() with plain Flow<T> ───────────────────────
    // For reactive streams (Room, DataStore) — auto-wrapped into Resource.
    private val _bookmarks = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
    val bookmarks: StateFlow<Resource<List<Article>>> = _bookmarks.asStateFlow()

    init {
        loadArticles()
        observeBookmarks()
        observeBookmarkCount()
    }

    /**
     * collectResource() — suspend one-shot:
     * Fetches articles from API, auto-handles Loading → Success/Error.
     */
    fun loadArticles() = collectResource(_articles) {
        getArticlesUseCase(currentState.selectedCategory)
    }

    /**
     * collectFlow() — plain Flow<T>:
     * Room returns Flow<List<Article>>, auto-wrapped into Resource.
     */
    private fun observeBookmarks() = collectFlow(_bookmarks, distinctUntilChanged = true) {
        getBookmarksUseCase()
    }

    /**
     * collect() — reactive stream with state reducer:
     * Derive bookmark count from Flow and map into HomeState.
     */
    private fun observeBookmarkCount() = collect(
        flow = {
            getBookmarksUseCase().asResource()
        },
        onSuccess = { copy(bookmarkCount = it.size) }
    )

    /**
     * execute() — one-shot action with state reducer:
     * Toggle bookmark, update articles list, send event.
     */
    fun toggleBookmark(article: Article) = execute(
        call = { Resource.Success(toggleBookmarkUseCase(article)) },
        onSuccess = {
            _articles.value.let { resource ->
                if (resource is Resource.Success) {
                    _articles.value = Resource.Success(
                        resource.data.map { a ->
                            if (a.id == article.id) a.copy(isBookmarked = it) else a
                        }
                    )
                }
            }
            this
        },
        errorEvent = { HomeEvent.ShowError(it.message ?: "Failed to toggle bookmark") }
    )

    fun selectCategory(category: ArticleCategory) {
        setState { copy(selectedCategory = category) }
        loadArticles()
    }

    fun onArticleClick(articleId: Long) {
        sendEvent(HomeEvent.NavigateToDetail(articleId))
    }
}
