package com.miru.sdk.sample.domain.repository

import com.miru.sdk.sample.domain.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for bookmark operations.
 * Backed by local Room database in the data layer.
 */
interface BookmarkRepository {

    /**
     * Observes all bookmarked articles reactively.
     */
    fun observeBookmarks(): Flow<List<Article>>

    /**
     * Checks if an article is bookmarked.
     */
    suspend fun isBookmarked(articleId: Long): Boolean

    /**
     * Adds an article to bookmarks.
     */
    suspend fun addBookmark(article: Article)

    /**
     * Removes an article from bookmarks.
     */
    suspend fun removeBookmark(articleId: Long)

    /**
     * Toggles the bookmark state of an article.
     * Returns true if the article is now bookmarked, false otherwise.
     */
    suspend fun toggleBookmark(article: Article): Boolean
}
