package com.miru.sdk.sample.data.source.remote

import com.miru.sdk.core.Resource
import com.miru.sdk.network.ApiService
import com.miru.sdk.sample.data.model.NewsApiResponse
import io.ktor.client.HttpClient

/**
 * Remote data source for articles using NewsAPI.org.
 *
 * Endpoints used:
 * - `top-headlines` — browse by category (country=us)
 * - `everything` — full-text search across all sources
 *
 * @param httpClient Ktor HTTP client with baseUrl = "https://newsapi.org/v2/"
 * @param apiKey NewsAPI.org API key
 */
class ArticleApi(
    httpClient: HttpClient,
    private val apiKey: String
) : ApiService(httpClient) {

    /**
     * Fetch top headlines by category.
     *
     * @see <a href="https://newsapi.org/docs/endpoints/top-headlines">top-headlines docs</a>
     */
    suspend fun getTopHeadlines(
        category: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Resource<NewsApiResponse> = get(
        path = "top-headlines",
        queryParams = mapOf(
            "category" to category,
            "country" to "us",
            "page" to page.toString(),
            "pageSize" to pageSize.toString(),
            "apiKey" to apiKey
        )
    )

    /**
     * Search articles across all sources.
     *
     * @see <a href="https://newsapi.org/docs/endpoints/everything">everything docs</a>
     */
    suspend fun searchArticles(
        query: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Resource<NewsApiResponse> = get(
        path = "everything",
        queryParams = mapOf(
            "q" to query,
            "sortBy" to "popularity",
            "page" to page.toString(),
            "pageSize" to pageSize.toString(),
            "apiKey" to apiKey
        )
    )
}
