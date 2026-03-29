package com.miru.sdk.sample.domain.model

import kotlinx.datetime.Instant

/**
 * Domain model representing a news article.
 * This is a pure business object with no framework dependencies.
 */
data class Article(
    val id: Long,
    val title: String,
    val description: String,
    val content: String,
    val author: String,
    val source: String,
    val imageUrl: String?,
    val articleUrl: String,
    val publishedAt: Instant,
    val isBookmarked: Boolean = false
)
