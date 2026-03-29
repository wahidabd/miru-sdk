package com.miru.sdk.sample.domain.usecase

import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow

/**
 * Observes all bookmarked articles reactively.
 */
class GetBookmarksUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    operator fun invoke(): Flow<List<Article>> {
        return bookmarkRepository.observeBookmarks()
    }
}
