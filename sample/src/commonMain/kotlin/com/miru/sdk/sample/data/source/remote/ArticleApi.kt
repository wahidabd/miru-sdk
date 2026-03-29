package com.miru.sdk.sample.data.source.remote

import com.miru.sdk.core.Resource
import com.miru.sdk.network.ApiService
import com.miru.sdk.network.model.ApiResponse
import com.miru.sdk.sample.data.model.ArticleDto
import io.ktor.client.HttpClient

/**
 * Remote data source for articles.
 * Extends the SDK's ApiService for automatic error handling via safeApiCall.
 */
class ArticleApi(httpClient: HttpClient) : ApiService(httpClient) {

    suspend fun getArticles(
        category: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Resource<ApiResponse<List<ArticleDto>>> = get(
        path = "articles",
        queryParams = mapOf(
            "category" to category,
            "page" to page.toString(),
            "pageSize" to pageSize.toString()
        )
    )

    suspend fun getArticleById(id: Long): Resource<ApiResponse<ArticleDto>> = get(
        path = "articles/$id"
    )

    suspend fun searchArticles(
        query: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Resource<ApiResponse<List<ArticleDto>>> = get(
        path = "articles/search",
        queryParams = mapOf(
            "q" to query,
            "page" to page.toString(),
            "pageSize" to pageSize.toString()
        )
    )
}
