package com.miru.sdk.ui.components.resource

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.miru.sdk.core.Resource
import com.miru.sdk.ui.components.error.MiruErrorView
import com.miru.sdk.ui.components.loading.MiruFullScreenLoading

/**
 * Renders UI based on a [Resource] state — handles Loading, Error, and Success automatically.
 *
 * Eliminates the repetitive `when (resource) { is Loading -> ... is Error -> ... is Success -> ... }`
 * pattern in every screen.
 *
 * Example:
 * ```
 * val articles by viewModel.articles.collectAsStateWithLifecycle()
 *
 * MiruResourceView(
 *     resource = articles,
 *     onRetry = { viewModel.loadArticles() }
 * ) { data ->
 *     LazyColumn {
 *         items(data) { article -> ArticleCard(article) }
 *     }
 * }
 * ```
 *
 * @param T The type of data in the Resource
 * @param resource The current Resource state
 * @param modifier Modifier applied to loading and error states
 * @param loadingMessage Optional message shown during loading
 * @param onRetry Optional retry callback for error state. If null, no retry button is shown.
 * @param onLoading Optional custom loading composable. Defaults to [MiruFullScreenLoading].
 * @param onError Optional custom error composable. Defaults to [MiruErrorView].
 * @param content Composable rendered when Resource is Success, receives the data
 */
@Composable
fun <T> MiruResourceView(
    resource: Resource<T>,
    modifier: Modifier = Modifier,
    loadingMessage: String? = null,
    onRetry: (() -> Unit)? = null,
    onLoading: (@Composable () -> Unit)? = null,
    onError: (@Composable (String) -> Unit)? = null,
    content: @Composable (T) -> Unit
) {
    when (resource) {
        is Resource.Loading -> {
            if (onLoading != null) {
                onLoading()
            } else {
                MiruFullScreenLoading(
                    message = loadingMessage ?: "",
                    modifier = modifier.fillMaxSize()
                )
            }
        }

        is Resource.Error -> {
            val message = resource.exception.message ?: "Something went wrong"
            if (onError != null) {
                onError(message)
            } else {
                MiruErrorView(
                    message = message,
                    onRetry = onRetry,
                    modifier = modifier.fillMaxSize()
                )
            }
        }

        is Resource.Success -> {
            content(resource.data)
        }
    }
}
