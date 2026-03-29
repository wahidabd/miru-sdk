package com.miru.sdk.sample.data.repository

import com.miru.sdk.core.Resource
import com.miru.sdk.core.map
import com.miru.sdk.core.mapper.mapWith
import com.miru.sdk.sample.data.mapper.ArticleDtoMapper
import com.miru.sdk.sample.data.source.remote.ArticleApi
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.model.ArticleCategory
import com.miru.sdk.sample.domain.repository.ArticleRepository

/**
 * Implementation of [ArticleRepository].
 * Bridges the data layer (API + DTOs) with the domain layer (Article models).
 */
class ArticleRepositoryImpl(
    private val api: ArticleApi,
    private val mapper: ArticleDtoMapper
) : ArticleRepository {

    override suspend fun getArticles(category: ArticleCategory, page: Int): Resource<List<Article>> {
        return api.getArticles(
            category = category.apiValue,
            page = page
        ).map { response ->
            response.data?.mapWith(mapper) ?: emptyList()
        }
    }

    override suspend fun getArticleById(id: Long): Resource<Article> {
        return api.getArticleById(id).map { response ->
            mapper.map(response.data!!)
        }
    }

    override suspend fun searchArticles(query: String, page: Int): Resource<List<Article>> {
        return api.searchArticles(query, page).map { response ->
            response.data?.mapWith(mapper) ?: emptyList()
        }
    }
}
