package com.miru.sdk.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.miru.sdk.ui.components.theme.MiruTheme

/**
 * Button style variants.
 */
enum class MiruButtonStyle {
    Primary,
    Secondary,
    Outline,
    Text,
    Danger,
}

/**
 * CMP Button composable with support for various styles and loading state.
 *
 * @param text The button text
 * @param onClick Called when the button is clicked
 * @param modifier The modifier to apply to the button
 * @param enabled Whether the button is enabled
 * @param loading Whether the button is in loading state, showing progress indicator
 * @param icon Optional icon to display next to the text
 * @param style The button style variant
 */
@Composable
fun MiruButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null,
    style: MiruButtonStyle = MiruButtonStyle.Primary,
) {
    val isClickable = enabled && !loading

    when (style) {
        MiruButtonStyle.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(48.dp),
                enabled = isClickable,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MiruTheme.colors.primary,
                    contentColor = MiruTheme.colors.onPrimary,
                    disabledContainerColor = MiruTheme.colors.primary.copy(alpha = 0.5f),
                    disabledContentColor = MiruTheme.colors.onPrimary.copy(alpha = 0.5f),
                ),
            ) {
                MiruButtonContent(
                    text = text,
                    icon = icon,
                    loading = loading,
                    contentColor = MiruTheme.colors.onPrimary,
                )
            }
        }

        MiruButtonStyle.Secondary -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(48.dp),
                enabled = isClickable,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MiruTheme.colors.secondary,
                    contentColor = MiruTheme.colors.onSecondary,
                    disabledContainerColor = MiruTheme.colors.secondary.copy(alpha = 0.5f),
                    disabledContentColor = MiruTheme.colors.onSecondary.copy(alpha = 0.5f),
                ),
            ) {
                MiruButtonContent(
                    text = text,
                    icon = icon,
                    loading = loading,
                    contentColor = MiruTheme.colors.onSecondary,
                )
            }
        }

        MiruButtonStyle.Outline -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(48.dp),
                enabled = isClickable,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MiruTheme.colors.primary,
                    disabledContentColor = MiruTheme.colors.primary.copy(alpha = 0.5f),
                ),
            ) {
                MiruButtonContent(
                    text = text,
                    icon = icon,
                    loading = loading,
                    contentColor = MiruTheme.colors.primary,
                )
            }
        }

        MiruButtonStyle.Text -> {
            TextButton(
                onClick = onClick,
                modifier = modifier.height(48.dp),
                enabled = isClickable,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MiruTheme.colors.primary,
                    disabledContentColor = MiruTheme.colors.primary.copy(alpha = 0.5f),
                ),
            ) {
                MiruButtonContent(
                    text = text,
                    icon = icon,
                    loading = loading,
                    contentColor = MiruTheme.colors.primary,
                )
            }
        }

        MiruButtonStyle.Danger -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(48.dp),
                enabled = isClickable,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MiruTheme.colors.error,
                    contentColor = MiruTheme.colors.onError,
                    disabledContainerColor = MiruTheme.colors.error.copy(alpha = 0.5f),
                    disabledContentColor = MiruTheme.colors.onError.copy(alpha = 0.5f),
                ),
            ) {
                MiruButtonContent(
                    text = text,
                    icon = icon,
                    loading = loading,
                    contentColor = MiruTheme.colors.onError,
                )
            }
        }
    }
}

/**
 * Button content with optional icon and loading state.
 */
@Composable
private fun MiruButtonContent(
    text: String,
    icon: ImageVector?,
    loading: Boolean,
    contentColor: Color,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = contentColor,
                strokeWidth = 2.dp,
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp),
                tint = contentColor,
            )
        }

        Text(text = text)
    }
}

/**
 * CMP Icon Button composable.
 *
 * @param icon The icon to display
 * @param onClick Called when the button is clicked
 * @param modifier The modifier to apply to the button
 * @param enabled Whether the button is enabled
 * @param contentDescription Accessibility description for the icon
 */
@Composable
fun MiruIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MiruTheme.colors.primary,
        )
    }
}
