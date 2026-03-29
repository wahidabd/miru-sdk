package com.miru.sdk.sample.presentation.home

import com.miru.sdk.core.onError
import com.miru.sdk.core.onSuccess
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.model.ArticleCategory
import com.miru.sdk.sample.domain.usecase.GetArticlesUseCase
import com.miru.sdk.sample.domain.usecase.ToggleBookmarkUseCase
import com.miru.sdk.ui.state.BaseViewModel

data class HomeState(
    val articles: List<Article> = emptyList(),
    val selectedCategory: ArticleCategory = ArticleCategory.GENERAL,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface HomeEvent {
    data class ShowError(val message: String) : HomeEvent
    data class NavigateToDetail(val articleId: Long) : HomeEvent
    data class BookmarkToggled(val isBookmarked: Boolean) : HomeEvent
}

class HomeViewModel(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<HomeState, HomeEvent>(HomeState()) {

    init {
        loadArticles()
    }

    fun loadArticles() = launch {
        setState { copy(isLoading = true, error = null) }

        getArticlesUseCase(currentState.selectedCategory)
            .onSuccess { articles ->
                setState { copy(articles = articles, isLoading = false) }
            }
            .onError { exception, _ ->
                setState { copy(isLoading = false, error = exception.message) }
                sendEvent(HomeEvent.ShowError(exception.message ?: "Failed to load articles"))
            }
    }

    fun selectCategory(category: ArticleCategory) {
        setState { copy(selectedCategory = category) }
        loadArticles()
    }

    fun onArticleClick(articleId: Long) {
        sendEvent(HomeEvent.NavigateToDetail(articleId))
    }

    fun toggleBookmark(article: Article) = launch {
        val isNowBookmarked = toggleBookmarkUseCase(article)

        // Update the article in the local list
        setState {
            copy(articles = articles.map {
                if (it.id == article.id) it.copy(isBookmarked = isNowBookmarked) else it
            })
        }
        sendEvent(HomeEvent.BookmarkToggled(isNowBookmarked))
    }
}
