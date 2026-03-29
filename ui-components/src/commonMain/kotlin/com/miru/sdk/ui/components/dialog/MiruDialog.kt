package com.miru.sdk.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.miru.sdk.ui.components.theme.MiruTheme

/**
 * CMP Alert Dialog composable.
 *
 * @param title The dialog title
 * @param message The dialog message
 * @param confirmText The confirm button text
 * @param dismissText The dismiss button text
 * @param onConfirm Called when confirm button is clicked
 * @param onDismiss Called when dismiss button is clicked or dialog is dismissed
 * @param icon Optional icon to display in the dialog
 */
@Composable
fun MiruAlertDialog(
    title: String,
    message: String,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    icon: ImageVector? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MiruTheme.typography.titleLarge,
                color = MiruTheme.colors.onSurface,
            )
        },
        text = {
            Column {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(bottom = 12.dp),
                        tint = MiruTheme.colors.primary,
                    )
                }
                Text(
                    text = message,
                    style = MiruTheme.typography.bodyMedium,
                    color = MiruTheme.colors.onSurface,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText, color = MiruTheme.colors.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText, color = MiruTheme.colors.onSurface.copy(alpha = 0.7f))
            }
        },
        containerColor = MiruTheme.colors.surface,
    )
}

/**
 * CMP Loading Dialog composable.
 *
 * @param message The message to display
 * @param onDismissRequest Called when the dialog is dismissed
 */
@Composable
fun MiruLoadingDialog(
    message: String = "Loading...",
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(
                    color = MiruTheme.colors.primary,
                    modifier = Modifier.size(40.dp),
                    strokeWidth = 3.dp,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = MiruTheme.typography.bodyMedium,
                    color = MiruTheme.colors.onSurface,
                )
            }
        },
        confirmButton = {},
        containerColor = MiruTheme.colors.surface,
    )
}

/**
 * CMP Confirmation Dialog composable.
 *
 * @param title The dialog title
 * @param message The dialog message
 * @param onConfirm Called when confirm button is clicked
 * @param onDismiss Called when dismiss button is clicked
 * @param confirmText The confirm button text
 * @param dismissText The dismiss button text
 * @param isDestructive Whether the confirm action is destructive (error color)
 */
@Composable
fun MiruConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    isDestructive: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MiruTheme.typography.titleLarge,
                color = MiruTheme.colors.onSurface,
            )
        },
        text = {
            Text(
                text = message,
                style = MiruTheme.typography.bodyMedium,
                color = MiruTheme.colors.onSurface,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    confirmText,
                    color = if (isDestructive) {
                        MiruTheme.colors.error
                    } else {
                        MiruTheme.colors.primary
                    },
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText, color = MiruTheme.colors.onSurface.copy(alpha = 0.7f))
            }
        },
        containerColor = MiruTheme.colors.surface,
    )
}
