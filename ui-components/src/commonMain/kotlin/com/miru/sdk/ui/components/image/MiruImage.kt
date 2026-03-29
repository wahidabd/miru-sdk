package com.miru.sdk.ui.components.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.miru.sdk.ui.components.theme.MiruTheme

/**
 * CMP Network Image composable using Coil3 for image loading.
 *
 * @param url The URL of the image to load
 * @param modifier The modifier to apply to the image
 * @param contentDescription Accessibility description for the image
 * @param contentScale How the image should be scaled
 * @param placeholder Composable to show while loading
 * @param error Composable to show on error
 */
@Composable
fun MiruNetworkImage(
    url: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: @Composable (() -> Unit)? = null,
    error: @Composable (() -> Unit)? = null,
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            if (placeholder != null) {
                placeholder()
            } else {
                NetworkImageLoadingPlaceholder()
            }
        },
        error = {
            if (error != null) {
                error()
            } else {
                NetworkImageErrorPlaceholder()
            }
        },
    )
}

/**
 * Default loading placeholder for MiruNetworkImage.
 */
@Composable
fun NetworkImageLoadingPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MiruTheme.colors.primary,
            modifier = Modifier.size(40.dp),
            strokeWidth = 2.dp,
        )
    }
}

/**
 * Default error placeholder for MiruNetworkImage.
 */
@Composable
fun NetworkImageErrorPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.BrokenImage,
            contentDescription = "Image failed to load",
            modifier = Modifier.size(48.dp),
            tint = MiruTheme.colors.error.copy(alpha = 0.6f),
        )
    }
}
