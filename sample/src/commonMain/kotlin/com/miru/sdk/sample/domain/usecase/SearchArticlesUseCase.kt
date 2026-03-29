package com.miru.sdk.sample.domain.usecase

import com.miru.sdk.core.Resource
import com.miru.sdk.core.map
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.repository.ArticleRepository
import com.miru.sdk.sample.domain.repository.BookmarkRepository

/**
 * Searches articles by keyword and enriches results with bookmark state.
 */
class SearchArticlesUseCase(
    private val articleRepository: ArticleRepository,
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(query: String, page: Int = 1): Resource<List<Article>> {
        return articleRepository.searchArticles(query, page).map { articles ->
            articles.map { article ->
                article.copy(isBookmarked = bookmarkRepository.isBookmarked(article.id))
            }
        }
    }
}
