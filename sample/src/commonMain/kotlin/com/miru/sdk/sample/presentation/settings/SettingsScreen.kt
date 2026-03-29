package com.miru.sdk.sample.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Dark Mode Toggle
            SettingsSwitch(
                title = "Dark Mode",
                subtitle = "Use dark theme throughout the app",
                checked = state.isDarkMode,
                onToggle = { viewModel.toggleDarkMode() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Notifications Toggle
            SettingsSwitch(
                title = "Notifications",
                subtitle = "Receive breaking news alerts",
                checked = state.notificationsEnabled,
                onToggle = { viewModel.toggleNotifications() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Font Size Selection
            Text(
                text = "Font Size",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))

            FontSize.entries.forEach { fontSize ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.setFontSize(fontSize) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = fontSize.displayName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    RadioButton(
                        selected = state.selectedFontSize == fontSize,
                        onClick = { viewModel.setFontSize(fontSize) }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // App info
            Text(
                text = "Miru News Sample",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Built with Miru SDK v0.1.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsSwitch(
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}
