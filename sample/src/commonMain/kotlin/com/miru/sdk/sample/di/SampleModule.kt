package com.miru.sdk.sample.di

import com.miru.sdk.sample.data.mapper.ArticleDtoMapper
import com.miru.sdk.sample.data.mapper.BookmarkMapper
import com.miru.sdk.sample.data.repository.ArticleRepositoryImpl
import com.miru.sdk.sample.data.repository.BookmarkRepositoryImpl
import com.miru.sdk.sample.data.source.local.SampleDatabase
import com.miru.sdk.sample.data.source.remote.ArticleApi
import com.miru.sdk.sample.domain.repository.ArticleRepository
import com.miru.sdk.sample.domain.repository.BookmarkRepository
import com.miru.sdk.sample.domain.usecase.GetArticleDetailUseCase
import com.miru.sdk.sample.domain.usecase.GetArticlesUseCase
import com.miru.sdk.sample.domain.usecase.GetBookmarksUseCase
import com.miru.sdk.sample.domain.usecase.SearchArticlesUseCase
import com.miru.sdk.sample.domain.usecase.ToggleBookmarkUseCase
import com.miru.sdk.sample.presentation.bookmark.BookmarkViewModel
import com.miru.sdk.sample.presentation.detail.DetailViewModel
import com.miru.sdk.sample.presentation.home.HomeViewModel
import com.miru.sdk.sample.presentation.search.SearchViewModel
import com.miru.sdk.sample.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin module for the sample app.
 *
 * Wires all three layers following clean architecture:
 * - Data: mappers, data sources, repository implementations
 * - Domain: use cases
 * - Presentation: ViewModels
 *
 * Requires a String qualifier named "newsApiKey" to be provided
 * (see [createSampleModule]).
 */
fun createSampleModule(newsApiKey: String) = module {

    // ── Data Layer ──────────────────────────────────────────

    // Database (platform-specific creation)
    single { createSampleDatabase() }

    // Mappers
    singleOf(::ArticleDtoMapper)
    singleOf(::BookmarkMapper)

    // Remote data source (requires HttpClient from :network module + API key)
    single { ArticleApi(httpClient = get(), apiKey = newsApiKey) }

    // Local data source (DAO from Room database)
    single { get<SampleDatabase>().bookmarkDao() }

    // Repository implementations (bound to domain interfaces)
    single<ArticleRepository> { ArticleRepositoryImpl(get(), get()) }
    single<BookmarkRepository> { BookmarkRepositoryImpl(get(), get()) }

    // ── Domain Layer ───────────────────────────────────────

    // Use cases
    factoryOf(::GetArticlesUseCase)
    factoryOf(::GetArticleDetailUseCase)
    factoryOf(::SearchArticlesUseCase)
    factoryOf(::ToggleBookmarkUseCase)
    factoryOf(::GetBookmarksUseCase)

    // ── Presentation Layer ─────────────────────────────────

    // ViewModels
    viewModelOf(::HomeViewModel)
    viewModelOf(::BookmarkViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::SearchViewModel)

    // DetailViewModel requires articleId parameter
    viewModel { params -> DetailViewModel(params.get(), get(), get()) }
}
