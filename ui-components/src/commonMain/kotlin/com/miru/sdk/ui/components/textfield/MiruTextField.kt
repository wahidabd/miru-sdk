package com.miru.sdk.ui.components.textfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.miru.sdk.ui.components.theme.MiruTheme

/**
 * CMP TextField composable.
 *
 * @param value The current text value
 * @param onValueChange Called when the text value changes
 * @param modifier The modifier to apply to the field
 * @param label The label text
 * @param placeholder The placeholder text
 * @param leadingIcon Optional leading icon
 * @param trailingIcon Optional trailing icon
 * @param isError Whether the field is in error state
 * @param errorMessage The error message to display
 * @param enabled Whether the field is enabled
 * @param readOnly Whether the field is read-only
 * @param singleLine Whether the field is single line
 * @param maxLines Maximum number of lines
 * @param keyboardOptions Keyboard options
 * @param keyboardActions Keyboard actions
 * @param visualTransformation Visual transformation for the text
 */
@Composable
fun MiruTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(modifier = modifier) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = if (label.isNotEmpty()) {
                { Text(label) }
            } else {
                null
            },
            placeholder = if (placeholder.isNotEmpty()) {
                { Text(placeholder) }
            } else {
                null
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MiruTheme.colors.surface,
                unfocusedContainerColor = MiruTheme.colors.surface,
                errorContainerColor = MiruTheme.colors.surface,
                focusedLabelColor = MiruTheme.colors.primary,
                unfocusedLabelColor = MiruTheme.colors.onSurface.copy(alpha = 0.6f),
                errorLabelColor = MiruTheme.colors.error,
                focusedIndicatorColor = MiruTheme.colors.primary,
                unfocusedIndicatorColor = MiruTheme.colors.onSurface.copy(alpha = 0.3f),
                errorIndicatorColor = MiruTheme.colors.error,
            ),
        )

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MiruTheme.colors.error,
                style = MiruTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            )
        }
    }
}

/**
 * CMP Password Field composable with visibility toggle.
 *
 * @param value The current password value
 * @param onValueChange Called when the password value changes
 * @param modifier The modifier to apply to the field
 * @param label The label text
 * @param placeholder The placeholder text
 * @param isError Whether the field is in error state
 * @param errorMessage The error message to display
 */
@Composable
fun MiruPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String = "Enter your password",
    isError: Boolean = false,
    errorMessage: String = "",
) {
    var passwordVisible by remember { mutableStateOf(false) }

    MiruTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    },
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = MiruTheme.colors.onSurface,
                )
            }
        },
    )
}

/**
 * CMP Search Field composable.
 *
 * @param query The current search query
 * @param onQueryChange Called when the query changes
 * @param onSearch Called when search is triggered
 * @param modifier The modifier to apply to the field
 * @param placeholder The placeholder text
 */
@Composable
fun MiruSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
) {
    MiruTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = placeholder,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MiruTheme.colors.onSurface.copy(alpha = 0.6f),
            )
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear search",
                        tint = MiruTheme.colors.onSurface,
                    )
                }
            }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
    )
}
