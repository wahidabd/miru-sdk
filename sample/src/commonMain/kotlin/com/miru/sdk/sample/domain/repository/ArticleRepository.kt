package com.miru.sdk.sample.domain.repository

import com.miru.sdk.core.Resource
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.model.ArticleCategory
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for article operations.
 *
 * Domain layer defines the interface — data layer provides the implementation.
 * This ensures the domain layer has zero knowledge of APIs, databases, or any data source.
 */
interface ArticleRepository {

    /**
     * Fetches articles from the remote API by category.
     */
    suspend fun getArticles(category: ArticleCategory, page: Int = 1): Resource<List<Article>>

    /**
     * Fetches a single article by its ID.
     */
    suspend fun getArticleById(id: Long): Resource<Article>

    /**
     * Searches articles by keyword.
     */
    suspend fun searchArticles(query: String, page: Int = 1): Resource<List<Article>>
}
