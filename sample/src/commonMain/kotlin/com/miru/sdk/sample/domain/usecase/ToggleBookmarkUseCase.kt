package com.miru.sdk.sample.domain.usecase

import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.repository.BookmarkRepository

/**
 * Toggles the bookmark state of an article.
 * Returns true if the article is now bookmarked, false if removed.
 */
class ToggleBookmarkUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(article: Article): Boolean {
        return bookmarkRepository.toggleBookmark(article)
    }
}
