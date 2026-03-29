package com.miru.sdk.sample.data.mapper

import com.miru.sdk.core.mapper.BidirectionalMapper
import com.miru.sdk.sample.data.model.BookmarkEntity
import com.miru.sdk.sample.domain.model.Article
import kotlinx.datetime.Instant
import kotlin.time.Clock

/**
 * Bidirectional mapper between BookmarkEntity (Room) ↔ Article (domain).
 */
class BookmarkMapper : BidirectionalMapper<BookmarkEntity, Article> {

    override fun map(from: BookmarkEntity): Article = Article(
        id = from.id,
        title = from.title,
        description = from.description,
        content = from.content,
        author = from.author,
        source = from.source,
        imageUrl = from.imageUrl,
        articleUrl = from.articleUrl,
        publishedAt = try {
            Instant.parse(from.publishedAt)
        } catch (_: Exception) {
            Instant.fromEpochMilliseconds(0)
        },
        isBookmarked = true
    )

    override fun reverseMap(to: Article): BookmarkEntity = BookmarkEntity(
        id = to.id,
        title = to.title,
        description = to.description,
        content = to.content,
        author = to.author,
        source = to.source,
        imageUrl = to.imageUrl,
        articleUrl = to.articleUrl,
        publishedAt = to.publishedAt.toString(),
        bookmarkedAt = Clock.System.now().toEpochMilliseconds()
    )
}
