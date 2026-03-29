package com.miru.sdk.sample.presentation.detail

import com.miru.sdk.core.onError
import com.miru.sdk.core.onSuccess
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.usecase.GetArticleDetailUseCase
import com.miru.sdk.sample.domain.usecase.ToggleBookmarkUseCase
import com.miru.sdk.ui.state.BaseViewModel

data class DetailState(
    val article: Article? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface DetailEvent {
    data class ShowError(val message: String) : DetailEvent
    data class BookmarkToggled(val isBookmarked: Boolean) : DetailEvent
}

class DetailViewModel(
    private val articleId: Long,
    private val getArticleDetailUseCase: GetArticleDetailUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<DetailState, DetailEvent>(DetailState()) {

    init {
        loadArticle()
    }

    fun loadArticle() = launch {
        setState { copy(isLoading = true, error = null) }

        getArticleDetailUseCase(articleId)
            .onSuccess { article ->
                setState { copy(article = article, isLoading = false) }
            }
            .onError { exception, _ ->
                setState { copy(isLoading = false, error = exception.message) }
                sendEvent(DetailEvent.ShowError(exception.message ?: "Failed to load article"))
            }
    }

    fun toggleBookmark() = launch {
        val article = currentState.article ?: return@launch
        val isNowBookmarked = toggleBookmarkUseCase(article)
        setState { copy(article = article.copy(isBookmarked = isNowBookmarked)) }
        sendEvent(DetailEvent.BookmarkToggled(isNowBookmarked))
    }
}
