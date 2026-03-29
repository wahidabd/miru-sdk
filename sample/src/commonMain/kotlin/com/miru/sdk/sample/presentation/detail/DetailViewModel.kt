package com.miru.sdk.sample.presentation.detail

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

    fun loadArticle() = execute(
        call = { getArticleDetailUseCase(articleId) },
        onLoading = { copy(isLoading = true, error = null) },
        onSuccess = { copy(article = it, isLoading = false) },
        onError = { copy(isLoading = false, error = it.message) },
        errorEvent = { DetailEvent.ShowError(it.message ?: "Failed to load article") }
    )

    fun toggleBookmark() = launch {
        val article = currentState.article ?: return@launch
        val isNowBookmarked = toggleBookmarkUseCase(article)
        setState { copy(article = article.copy(isBookmarked = isNowBookmarked)) }
        sendEvent(DetailEvent.BookmarkToggled(isNowBookmarked))
    }
}
