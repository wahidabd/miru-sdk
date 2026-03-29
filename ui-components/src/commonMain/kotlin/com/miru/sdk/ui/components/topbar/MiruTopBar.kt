package com.miru.sdk.ui.components.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.miru.sdk.ui.components.textfield.MiruTextField
import com.miru.sdk.ui.components.theme.MiruTheme

/**
 * CMP Top App Bar composable.
 *
 * @param title The title text
 * @param modifier The modifier to apply to the top bar
 * @param navigationIcon Optional navigation icon
 * @param onNavigationClick Called when navigation icon is clicked
 * @param actions Optional action composables
 * @param colors The colors to use for the top bar
 */
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MiruTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MiruTheme.colors.primary,
        titleContentColor = MiruTheme.colors.onPrimary,
        navigationIconContentColor = MiruTheme.colors.onPrimary,
        actionIconContentColor = MiruTheme.colors.onPrimary,
    ),
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MiruTheme.typography.titleLarge,
            )
        },
        modifier = modifier,
        navigationIcon = if (navigationIcon != null && onNavigationClick != null) {
            {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navigation",
                    )
                }
            }
        } else {
            {}
        },
        actions = actions ?: {},
        colors = colors,
    )
}

/**
 * CMP Search Top App Bar composable.
 *
 * @param query The current search query
 * @param onQueryChange Called when the query changes
 * @param onBack Called when the back button is clicked
 * @param onSearch Called when search is triggered
 * @param placeholder The placeholder text for the search field
 * @param modifier The modifier to apply to the top bar
 */
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MiruSearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onSearch: (String) -> Unit,
    placeholder: String = "Search...",
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            MiruTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier,
                placeholder = placeholder,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = MiruTheme.colors.onPrimary,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch(query) },
                ),
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MiruTheme.colors.onPrimary,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MiruTheme.colors.primary,
            navigationIconContentColor = MiruTheme.colors.onPrimary,
        ),
    )
}
