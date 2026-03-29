package com.miru.sdk.sample.data.repository

import com.miru.sdk.core.AppException
import com.miru.sdk.core.Resource
import com.miru.sdk.core.map
import com.miru.sdk.core.mapper.mapWith
import com.miru.sdk.sample.data.mapper.ArticleDtoMapper
import com.miru.sdk.sample.data.source.remote.ArticleApi
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.model.ArticleCategory
import com.miru.sdk.sample.domain.repository.ArticleRepository

/**
 * Implementation of [ArticleRepository] backed by NewsAPI.org.
 *
 * Maintains an in-memory article cache so that the detail screen can
 * look up a previously fetched article by ID without a separate API call
 * (NewsAPI doesn't have a "get article by ID" endpoint).
 */
class ArticleRepositoryImpl(
    private val api: ArticleApi,
    private val mapper: ArticleDtoMapper
) : ArticleRepository {

    /** In-memory cache: articleId → Article */
    private val articleCache = mutableMapOf<Long, Article>()

    override suspend fun getArticles(
        category: ArticleCategory,
        page: Int
    ): Resource<List<Article>> {
        return api.getTopHeadlines(
            category = category.apiValue,
            page = page
        ).map { response ->
            val articles = response.articles
                .filter { !it.title.isNullOrBlank() && it.title != "[Removed]" }
                .mapWith(mapper)
            // Populate cache
            articles.forEach { articleCache[it.id] = it }
            articles
        }
    }

    override suspend fun getArticleById(id: Long): Resource<Article> {
        val cached = articleCache[id]
        return if (cached != null) {
            Resource.Success(cached)
        } else {
            Resource.Error(AppException.NotFoundException("Article not found in cache"))
        }
    }

    override suspend fun searchArticles(
        query: String,
        page: Int
    ): Resource<List<Article>> {
        return api.searchArticles(query, page).map { response ->
            val articles = response.articles
                .filter { !it.title.isNullOrBlank() && it.title != "[Removed]" }
                .mapWith(mapper)
            // Populate cache
            articles.forEach { articleCache[it.id] = it }
            articles
        }
    }
}
