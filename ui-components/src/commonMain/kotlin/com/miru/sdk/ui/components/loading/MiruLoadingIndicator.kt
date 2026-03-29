package com.miru.sdk.ui.components.loading

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.miru.sdk.ui.components.theme.MiruTheme

/**
 * CMP Loading Indicator composable.
 *
 * @param modifier The modifier to apply to the indicator
 * @param size The size of the indicator
 * @param color The color of the indicator
 * @param strokeWidth The stroke width of the progress indicator
 */
@Composable
fun MiruLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MiruTheme.colors.primary,
    strokeWidth: Dp = 4.dp,
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        color = color,
        strokeWidth = strokeWidth,
    )
}

/**
 * CMP Full Screen Loading composable.
 *
 * @param message Optional message to display below the loading indicator
 * @param modifier The modifier to apply to the container
 * @param backgroundColor The background color
 */
@Composable
fun MiruFullScreenLoading(
    message: String = "",
    modifier: Modifier = Modifier,
    backgroundColor: Color = MiruTheme.colors.background,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MiruLoadingIndicator(
                size = 56.dp,
                color = MiruTheme.colors.primary,
            )

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    style = MiruTheme.typography.bodyMedium,
                    color = MiruTheme.colors.onBackground,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }
    }
}

/**
 * CMP Shimmer Effect composable for loading placeholders.
 *
 * @param modifier The modifier to apply to the shimmer effect
 * @param shape The shape of the shimmer
 * @param baseColor The base color
 * @param shimmerColor The shimmer highlight color
 */
@Composable
fun MiruShimmerEffect(
    modifier: Modifier = Modifier,
    shape: androidx.compose.foundation.shape.RoundedCornerShape = MiruTheme.shapes.medium,
    baseColor: Color = MiruTheme.colors.surface,
    shimmerColor: Color = MiruTheme.colors.onSurface.copy(alpha = 0.1f),
) {
    val infiniteTransition = rememberInfiniteTransition()

    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
        ),
    )

    Box(
        modifier = modifier
            .background(baseColor, shape = shape)
            .graphicsLayer {
                alpha = shimmerAlpha
            },
    )
}
