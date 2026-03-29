package com.miru.sdk.sample.presentation.bookmark

import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.usecase.GetBookmarksUseCase
import com.miru.sdk.sample.domain.usecase.ToggleBookmarkUseCase
import com.miru.sdk.ui.state.BaseViewModel

data class BookmarkState(
    val bookmarks: List<Article> = emptyList(),
    val isLoading: Boolean = true
)

sealed interface BookmarkEvent {
    data class NavigateToDetail(val articleId: Long) : BookmarkEvent
    data class BookmarkRemoved(val title: String) : BookmarkEvent
}

class BookmarkViewModel(
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<BookmarkState, BookmarkEvent>(BookmarkState()) {

    init {
        observeBookmarks()
    }

    private fun observeBookmarks() = launch {
        getBookmarksUseCase().collect { bookmarks ->
            setState { copy(bookmarks = bookmarks, isLoading = false) }
        }
    }

    fun onArticleClick(articleId: Long) {
        sendEvent(BookmarkEvent.NavigateToDetail(articleId))
    }

    fun removeBookmark(article: Article) = launch {
        toggleBookmarkUseCase(article)
        sendEvent(BookmarkEvent.BookmarkRemoved(article.title))
    }
}
