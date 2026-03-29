package com.miru.sdk.sample.domain.usecase

import com.miru.sdk.core.Resource
import com.miru.sdk.core.map
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.model.ArticleCategory
import com.miru.sdk.sample.domain.repository.ArticleRepository
import com.miru.sdk.sample.domain.repository.BookmarkRepository

/**
 * Fetches articles by category and enriches them with bookmark state.
 *
 * This use case demonstrates the single-responsibility principle —
 * it combines data from two repositories (remote articles + local bookmarks)
 * into a unified result.
 */
class GetArticlesUseCase(
    private val articleRepository: ArticleRepository,
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(
        category: ArticleCategory,
        page: Int = 1
    ): Resource<List<Article>> {
        return articleRepository.getArticles(category, page).map { articles ->
            articles.map { article ->
                article.copy(isBookmarked = bookmarkRepository.isBookmarked(article.id))
            }
        }
    }
}
