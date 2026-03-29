package com.miru.sdk.sample.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for article API responses.
 * Maps to the remote API JSON structure.
 */
@Serializable
data class ArticleDto(
    val id: Long = 0,
    val title: String = "",
    val description: String? = null,
    val content: String? = null,
    val author: String? = null,
    val source: SourceDto? = null,
    @SerialName("urlToImage")
    val imageUrl: String? = null,
    val url: String = "",
    @SerialName("publishedAt")
    val publishedAt: String = ""
)

@Serializable
data class SourceDto(
    val id: String? = null,
    val name: String = ""
)
