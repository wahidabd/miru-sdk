package com.miru.sdk.ui.components.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.miru.sdk.ui.components.theme.MiruTheme

/**
 * CMP Bottom Sheet composable.
 *
 * @param onDismiss Called when the bottom sheet is dismissed
 * @param modifier The modifier to apply to the sheet
 * @param sheetState The sheet state
 * @param content The content of the bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiruBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = MiruTheme.colors.surface,
        scrimColor = MiruTheme.colors.onBackground.copy(alpha = 0.32f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            content()
        }
    }
}

/**
 * CMP Modal Bottom Sheet composable with title and actions.
 *
 * @param title The title text
 * @param onDismiss Called when the bottom sheet is dismissed
 * @param actions Optional action composables
 * @param content The content of the bottom sheet
 * @param modifier The modifier to apply to the sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiruModalBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    actions: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    MiruBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MiruTheme.typography.titleLarge,
                color = MiruTheme.colors.onSurface,
                modifier = Modifier.weight(1f),
            )

            actions?.let {
                Row(content = it)
            }

            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = MiruTheme.colors.onSurface,
                )
            }
        }

        content()
    }
}
