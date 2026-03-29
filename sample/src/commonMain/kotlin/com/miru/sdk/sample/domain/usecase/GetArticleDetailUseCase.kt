package com.miru.sdk.sample.domain.usecase

import com.miru.sdk.core.Resource
import com.miru.sdk.core.map
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.repository.ArticleRepository
import com.miru.sdk.sample.domain.repository.BookmarkRepository

/**
 * Fetches a single article detail and enriches it with bookmark state.
 */
class GetArticleDetailUseCase(
    private val articleRepository: ArticleRepository,
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(articleId: Long): Resource<Article> {
        return articleRepository.getArticleById(articleId).map { article ->
            article.copy(isBookmarked = bookmarkRepository.isBookmarked(article.id))
        }
    }
}
