package com.miru.sdk.ui.components.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.miru.sdk.ui.components.button.MiruButton
import com.miru.sdk.ui.components.button.MiruButtonStyle
import com.miru.sdk.ui.components.spacer.SpacerMedium
import com.miru.sdk.ui.components.theme.MiruTheme

/**
 * CMP Error View composable.
 *
 * @param message The error message to display
 * @param onRetry Called when the retry button is clicked
 * @param modifier The modifier to apply to the view
 * @param icon Optional icon to display
 * @param retryText The text for the retry button
 */
@Composable
fun MiruErrorView(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.ErrorOutline,
    retryText: String = "Retry",
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Error",
            modifier = Modifier.size(56.dp),
            tint = MiruTheme.colors.error,
        )

        SpacerMedium()

        Text(
            text = message,
            style = MiruTheme.typography.bodyLarge,
            color = MiruTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(),
        )

        if (onRetry != null) {
            SpacerMedium()

            MiruButton(
                text = retryText,
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(),
                style = MiruButtonStyle.Primary,
            )
        }
    }
}

/**
 * CMP Empty View composable.
 *
 * @param title The title text
 * @param message The message text
 * @param modifier The modifier to apply to the view
 * @param icon Optional icon to display
 * @param actionText The text for the action button
 * @param onAction Called when the action button is clicked
 */
@Composable
fun MiruEmptyView(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Info,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = MiruTheme.colors.primary.copy(alpha = 0.5f),
        )

        SpacerMedium()

        Text(
            text = title,
            style = MiruTheme.typography.titleLarge,
            color = MiruTheme.colors.onBackground,
        )

        SpacerMedium()

        Text(
            text = message,
            style = MiruTheme.typography.bodyMedium,
            color = MiruTheme.colors.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.fillMaxWidth(),
        )

        if (actionText != null && onAction != null) {
            SpacerMedium()

            MiruButton(
                text = actionText,
                onClick = onAction,
                modifier = Modifier.fillMaxWidth(),
                style = MiruButtonStyle.Primary,
            )
        }
    }
}
