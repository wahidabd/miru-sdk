package com.miru.sdk.sample.data.mapper

import com.miru.sdk.core.mapper.Mapper
import com.miru.sdk.sample.data.model.ArticleDto
import com.miru.sdk.sample.domain.model.Article
import kotlinx.datetime.Instant
import kotlin.math.abs

/**
 * Maps ArticleDto (NewsAPI response) → Article (domain model).
 *
 * Since NewsAPI doesn't provide numeric IDs, we derive a stable ID
 * from the article URL hash so bookmarks can reference articles consistently.
 */
class ArticleDtoMapper : Mapper<ArticleDto, Article> {
    override fun map(from: ArticleDto): Article = Article(
        id = abs(from.url.hashCode().toLong()),
        title = from.title ?: "Untitled",
        description = from.description.orEmpty(),
        content = from.content.orEmpty(),
        author = from.author ?: "Unknown",
        source = from.source?.name ?: "Unknown",
        imageUrl = from.urlToImage,
        articleUrl = from.url,
        publishedAt = try {
            Instant.parse(from.publishedAt)
        } catch (_: Exception) {
            Instant.fromEpochMilliseconds(0)
        },
        isBookmarked = false
    )
}
