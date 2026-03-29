package com.miru.sdk.sample.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miru.sdk.ui.components.error.MiruErrorView
import com.miru.sdk.ui.components.loading.MiruFullScreenLoading
import com.miru.sdk.ui.components.theme.MiruTheme
import com.miru.sdk.ui.state.collectAsEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.events.collectAsEffect { event ->
        when (event) {
            is DetailEvent.ShowError -> { /* show snackbar */ }
            is DetailEvent.BookmarkToggled -> { /* optional feedback */ }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Article") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    state.article?.let { article ->
                        IconButton(onClick = { viewModel.toggleBookmark() }) {
                            Icon(
                                imageVector = if (article.isBookmarked) Icons.Default.Bookmark
                                else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (article.isBookmarked) MiruTheme.colors.primary
                                else MiruTheme.colors.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                MiruFullScreenLoading(
                    message = "Loading article...",
                    modifier = Modifier.padding(padding)
                )
            }
            state.error != null -> {
                MiruErrorView(
                    message = state.error ?: "Failed to load article",
                    onRetry = { viewModel.loadArticle() },
                    modifier = Modifier.fillMaxSize().padding(padding)
                )
            }
            state.article != null -> {
                val article = state.article!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Title
                    Text(
                        text = article.title,
                        style = MiruTheme.typography.headlineSmall,
                        color = MiruTheme.colors.onBackground,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    // Source & Author
                    Text(
                        text = "${article.source} • ${article.author}",
                        style = MiruTheme.typography.labelLarge,
                        color = MiruTheme.colors.primary
                    )

                    Spacer(Modifier.height(4.dp))

                    // Published date
                    Text(
                        text = article.publishedAt.toString(),
                        style = MiruTheme.typography.labelSmall,
                        color = MiruTheme.colors.onBackground.copy(alpha = 0.5f)
                    )

                    Spacer(Modifier.height(16.dp))

                    // Description
                    if (article.description.isNotBlank()) {
                        Text(
                            text = article.description,
                            style = MiruTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MiruTheme.colors.onBackground.copy(alpha = 0.8f)
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    // Content
                    Text(
                        text = article.content,
                        style = MiruTheme.typography.bodyMedium,
                        color = MiruTheme.colors.onBackground
                    )
                }
            }
        }
    }
}
