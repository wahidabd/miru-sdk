package com.miru.sdk.sample.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.ui.components.card.MiruCard
import com.miru.sdk.ui.components.error.MiruEmptyView
import com.miru.sdk.ui.components.error.MiruErrorView
import com.miru.sdk.ui.components.loading.MiruFullScreenLoading
import com.miru.sdk.ui.components.theme.MiruTheme
import com.miru.sdk.ui.state.collectAsEffect
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.events.collectAsEffect { event ->
        when (event) {
            is SearchEvent.NavigateToDetail -> onNavigateToDetail(event.articleId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search articles...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { viewModel.search() }),
                shape = MiruTheme.shapes.medium
            )

            when {
                state.isLoading -> {
                    MiruFullScreenLoading(message = "Searching...")
                }
                state.error != null -> {
                    MiruErrorView(
                        message = state.error ?: "Search failed",
                        onRetry = { viewModel.search() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                state.hasSearched && state.results.isEmpty() -> {
                    MiruEmptyView(
                        title = "No results",
                        message = "No articles found for \"${state.query}\". Try a different search term.",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                !state.hasSearched -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Search for news articles",
                            style = MiruTheme.typography.bodyMedium,
                            color = MiruTheme.colors.onBackground.copy(alpha = 0.5f)
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.results, key = { it.id }) { article ->
                            SearchResultCard(
                                article = article,
                                onClick = { viewModel.onArticleClick(article.id) },
                                onBookmarkClick = { viewModel.toggleBookmark(article) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultCard(
    article: Article,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    MiruCard(onClick = onClick) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title,
                    style = MiruTheme.typography.titleSmall,
                    color = MiruTheme.colors.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = article.description,
                    style = MiruTheme.typography.bodySmall,
                    color = MiruTheme.colors.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = article.source,
                    style = MiruTheme.typography.labelSmall,
                    color = MiruTheme.colors.primary
                )
            }

            Spacer(Modifier.width(8.dp))

            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = if (article.isBookmarked) Icons.Default.Bookmark
                    else Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (article.isBookmarked) MiruTheme.colors.primary
                    else MiruTheme.colors.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}
