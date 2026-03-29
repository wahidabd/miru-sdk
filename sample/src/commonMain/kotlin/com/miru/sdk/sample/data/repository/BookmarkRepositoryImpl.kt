package com.miru.sdk.sample.data.repository

import com.miru.sdk.core.mapper.mapWith
import com.miru.sdk.sample.data.mapper.BookmarkMapper
import com.miru.sdk.sample.data.source.local.BookmarkDao
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [BookmarkRepository].
 * Uses Room DAO for local persistence with bidirectional mapping.
 */
class BookmarkRepositoryImpl(
    private val dao: BookmarkDao,
    private val mapper: BookmarkMapper
) : BookmarkRepository {

    override fun observeBookmarks(): Flow<List<Article>> {
        return dao.observeAll().map { entities ->
            entities.mapWith(mapper)
        }
    }

    override suspend fun isBookmarked(articleId: Long): Boolean {
        return dao.isBookmarked(articleId)
    }

    override suspend fun addBookmark(article: Article) {
        dao.insert(mapper.reverseMap(article))
    }

    override suspend fun removeBookmark(articleId: Long) {
        dao.deleteById(articleId)
    }

    override suspend fun toggleBookmark(article: Article): Boolean {
        return if (dao.isBookmarked(article.id)) {
            dao.deleteById(article.id)
            false
        } else {
            dao.insert(mapper.reverseMap(article))
            true
        }
    }
}
