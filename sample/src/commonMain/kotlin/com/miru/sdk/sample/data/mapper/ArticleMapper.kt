package com.miru.sdk.sample.data.mapper

import com.miru.sdk.core.mapper.Mapper
import com.miru.sdk.sample.data.model.ArticleDto
import com.miru.sdk.sample.domain.model.Article
import kotlinx.datetime.Instant

/**
 * Maps ArticleDto (API response) → Article (domain model).
 */
class ArticleDtoMapper : Mapper<ArticleDto, Article> {
    override fun map(from: ArticleDto): Article = Article(
        id = from.id,
        title = from.title,
        description = from.description.orEmpty(),
        content = from.content.orEmpty(),
        author = from.author ?: "Unknown",
        source = from.source?.name ?: "Unknown",
        imageUrl = from.imageUrl,
        articleUrl = from.url,
        publishedAt = try {
            Instant.parse(from.publishedAt)
        } catch (_: Exception) {
            Instant.fromEpochMilliseconds(0)
        },
        isBookmarked = false
    )
}
