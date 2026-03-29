package com.miru.sdk.sample.presentation.settings

import com.miru.sdk.persistent.preferences.MiruPreferences
import com.miru.sdk.ui.state.BaseViewModel

data class SettingsState(
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val selectedFontSize: FontSize = FontSize.MEDIUM
)

enum class FontSize(val displayName: String, val scale: Float) {
    SMALL("Small", 0.85f),
    MEDIUM("Medium", 1.0f),
    LARGE("Large", 1.15f)
}

sealed interface SettingsEvent {
    data class ThemeChanged(val isDark: Boolean) : SettingsEvent
}

/**
 * SettingsViewModel demonstrates MiruPreferences (DataStore) usage.
 * Preferences are persisted locally and survive app restarts.
 */
class SettingsViewModel(
    private val preferences: MiruPreferences
) : BaseViewModel<SettingsState, SettingsEvent>(SettingsState()) {

    init {
        loadSettings()
    }

    private fun loadSettings() = launch {
        val isDark = preferences.getBoolean(KEY_DARK_MODE, false)
        val notifications = preferences.getBoolean(KEY_NOTIFICATIONS, true)
        val fontSizeOrdinal = preferences.getInt(KEY_FONT_SIZE, FontSize.MEDIUM.ordinal)

        setState {
            copy(
                isDarkMode = isDark,
                notificationsEnabled = notifications,
                selectedFontSize = FontSize.entries.getOrElse(fontSizeOrdinal) { FontSize.MEDIUM }
            )
        }
    }

    fun toggleDarkMode() = launch {
        val newValue = !currentState.isDarkMode
        preferences.putBoolean(KEY_DARK_MODE, newValue)
        setState { copy(isDarkMode = newValue) }
        sendEvent(SettingsEvent.ThemeChanged(newValue))
    }

    fun toggleNotifications() = launch {
        val newValue = !currentState.notificationsEnabled
        preferences.putBoolean(KEY_NOTIFICATIONS, newValue)
        setState { copy(notificationsEnabled = newValue) }
    }

    fun setFontSize(fontSize: FontSize) = launch {
        preferences.putInt(KEY_FONT_SIZE, fontSize.ordinal)
        setState { copy(selectedFontSize = fontSize) }
    }

    companion object {
        private const val KEY_DARK_MODE = "settings_dark_mode"
        private const val KEY_NOTIFICATIONS = "settings_notifications"
        private const val KEY_FONT_SIZE = "settings_font_size"
    }
}
