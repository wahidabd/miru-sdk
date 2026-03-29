package com.miru.sdk.sample.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
                                tint = if (article.isBookmarked) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading...")
                }
            }
            state.error != null -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.error ?: "Unknown error")
                }
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
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    // Source & Author
                    Text(
                        text = "${article.source} • ${article.author}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(4.dp))

                    // Published date
                    Text(
                        text = article.publishedAt.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(16.dp))

                    // Description
                    if (article.description.isNotBlank()) {
                        Text(
                            text = article.description,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    // Content
                    Text(
                        text = article.content,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    )
                }
            }
        }
    }
}
