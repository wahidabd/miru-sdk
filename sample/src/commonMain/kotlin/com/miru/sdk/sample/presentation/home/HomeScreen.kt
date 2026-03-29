package com.miru.sdk.sample.presentation.home

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.sample.domain.model.ArticleCategory
import com.miru.sdk.ui.state.collectAsEffect
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToBookmarks: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.events.collectAsEffect { event ->
        when (event) {
            is HomeEvent.NavigateToDetail -> onNavigateToDetail(event.articleId)
            is HomeEvent.ShowError -> { /* show snackbar */ }
            is HomeEvent.BookmarkToggled -> { /* optional feedback */ }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Miru News", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onNavigateToBookmarks) {
                        Icon(Icons.Default.Bookmarks, contentDescription = "Bookmarks")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Category chips
            CategoryChips(
                categories = ArticleCategory.entries,
                selected = state.selectedCategory,
                onSelect = viewModel::selectCategory
            )

            // Article list
            when {
                state.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Loading articles...")
                    }
                }
                state.error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.error ?: "Unknown error")
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Tap to retry",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { viewModel.loadArticles() }
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.articles, key = { it.id }) { article ->
                            ArticleCard(
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
private fun CategoryChips(
    categories: List<ArticleCategory>,
    selected: ArticleCategory,
    onSelect: (ArticleCategory) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = category == selected,
                onClick = { onSelect(category) },
                label = { Text(category.displayName) }
            )
        }
    }
}

@Composable
private fun ArticleCard(
    article: Article,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            // Description
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(12.dp))

            // Footer: source + bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = article.source,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = article.author,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onBookmarkClick) {
                    Icon(
                        imageVector = if (article.isBookmarked) Icons.Default.Bookmark
                        else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (article.isBookmarked) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
