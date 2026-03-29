package com.miru.sdk.sample.presentation.search

import com.miru.sdk.core.onError
import com.miru.sdk.core.onSuccess
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.usecase.SearchArticlesUseCase
import com.miru.sdk.sample.domain.usecase.ToggleBookmarkUseCase
import com.miru.sdk.ui.state.BaseViewModel

data class SearchState(
    val query: String = "",
    val results: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val error: String? = null
)

sealed interface SearchEvent {
    data class NavigateToDetail(val articleId: Long) : SearchEvent
}

class SearchViewModel(
    private val searchArticlesUseCase: SearchArticlesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<SearchState, SearchEvent>(SearchState()) {

    fun onQueryChanged(query: String) {
        setState { copy(query = query) }
    }

    fun search() = launch {
        val query = currentState.query.trim()
        if (query.isBlank()) return@launch

        setState { copy(isLoading = true, error = null, hasSearched = true) }

        searchArticlesUseCase(query)
            .onSuccess { articles ->
                setState { copy(results = articles, isLoading = false) }
            }
            .onError { exception, _ ->
                setState { copy(isLoading = false, error = exception.message) }
            }
    }

    fun onArticleClick(articleId: Long) {
        sendEvent(SearchEvent.NavigateToDetail(articleId))
    }

    fun toggleBookmark(article: Article) = launch {
        val isNowBookmarked = toggleBookmarkUseCase(article)
        setState {
            copy(results = results.map {
                if (it.id == article.id) it.copy(isBookmarked = isNowBookmarked) else it
            })
        }
    }
}
