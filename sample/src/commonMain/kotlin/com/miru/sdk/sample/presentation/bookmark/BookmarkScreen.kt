package com.miru.sdk.sample.presentation.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miru.sdk.sample.domain.model.Article
import com.miru.sdk.ui.components.card.MiruCard
import com.miru.sdk.ui.components.error.MiruEmptyView
import com.miru.sdk.ui.components.theme.MiruTheme
import com.miru.sdk.ui.state.collectAsEffect
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: BookmarkViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.events.collectAsEffect { event ->
        when (event) {
            is BookmarkEvent.NavigateToDetail -> onNavigateToDetail(event.articleId)
            is BookmarkEvent.BookmarkRemoved -> { /* show snackbar */ }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Bookmarks",
                        fontWeight = FontWeight.Bold,
                        color = MiruTheme.colors.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (state.bookmarks.isEmpty() && !state.isLoading) {
            MiruEmptyView(
                title = "No bookmarks yet",
                message = "Articles you bookmark will appear here. Tap the bookmark icon on any article to save it for later.",
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.bookmarks, key = { it.id }) { article ->
                    BookmarkCard(
                        article = article,
                        onClick = { viewModel.onArticleClick(article.id) },
                        onRemove = { viewModel.removeBookmark(article) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookmarkCard(
    article: Article,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    MiruCard(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
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
                    text = "${article.source} • ${article.author}",
                    style = MiruTheme.typography.labelSmall,
                    color = MiruTheme.colors.onSurface.copy(alpha = 0.5f)
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove bookmark",
                    tint = MiruTheme.colors.error
                )
            }
        }
    }
}
