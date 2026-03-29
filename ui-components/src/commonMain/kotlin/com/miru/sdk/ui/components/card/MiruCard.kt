package com.miru.sdk.ui.components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.miru.sdk.ui.components.theme.MiruTheme

/**
 * CMP Card composable.
 *
 * @param modifier The modifier to apply to the card
 * @param onClick Optional click callback
 * @param elevation The elevation of the card
 * @param shape The shape of the card
 * @param content The content of the card
 */
@Composable
fun MiruCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    elevation: androidx.compose.material3.CardElevation = CardDefaults.cardElevation(
        defaultElevation = 2.dp,
    ),
    shape: Shape = MiruTheme.shapes.medium,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = if (onClick != null) {
            modifier.clickable(onClick = onClick)
        } else {
            modifier
        },
        elevation = elevation,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MiruTheme.colors.surface,
            contentColor = MiruTheme.colors.onSurface,
        ),
    ) {
        Column(content = content)
    }
}

/**
 * CMP Info Card composable with title, subtitle, and optional icon/trailing content.
 *
 * @param title The title text
 * @param subtitle The subtitle text
 * @param leadingIcon Optional leading icon composable
 * @param trailingContent Optional trailing content composable
 * @param onClick Optional click callback
 * @param modifier The modifier to apply to the card
 */
@Composable
fun MiruInfoCard(
    title: String,
    subtitle: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    MiruCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                leadingIcon()
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = if (leadingIcon != null) 12.dp else 0.dp),
            ) {
                Text(
                    text = title,
                    style = MiruTheme.typography.titleMedium,
                    color = MiruTheme.colors.onSurface,
                )

                Text(
                    text = subtitle,
                    style = MiruTheme.typography.bodySmall,
                    color = MiruTheme.colors.onSurface.copy(alpha = 0.7f),
                )
            }

            if (trailingContent != null) {
                Row(content = trailingContent)
            }
        }
    }
}
