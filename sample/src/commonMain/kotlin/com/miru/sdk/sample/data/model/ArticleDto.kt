package com.miru.sdk.sample.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Top-level response wrapper from the NewsAPI.org API.
 *
 * Example response:
 * ```json
 * {
 *   "status": "ok",
 *   "totalResults": 123,
 *   "articles": [...]
 * }
 * ```
 */
@Serializable
data class NewsApiResponse(
    val status: String = "",
    val totalResults: Int = 0,
    val articles: List<ArticleDto> = emptyList()
)

/**
 * Data Transfer Object for a single article from NewsAPI.org.
 */
@Serializable
data class ArticleDto(
    val source: SourceDto? = null,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String = "",
    @SerialName("urlToImage")
    val urlToImage: String? = null,
    val publishedAt: String = "",
    val content: String? = null
)

@Serializable
data class SourceDto(
    val id: String? = null,
    val name: String = ""
)
