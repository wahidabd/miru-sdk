package com.miru.sdk.persistent.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Miru Preferences — a convenient wrapper around [DataStore]<[Preferences]>.
 *
 * Provides type-safe getters/setters for common types with both
 * reactive ([Flow]) and suspend (one-shot) access patterns.
 *
 * Usage:
 * ```
 * val prefs: MiruPreferences = get() // via Koin
 *
 * // Write
 * prefs.putString("user_name", "Wahid")
 * prefs.putBoolean("dark_mode", true)
 *
 * // Read (one-shot)
 * val name = prefs.getString("user_name", "Guest")
 *
 * // Read (reactive Flow)
 * prefs.observeString("user_name", "Guest").collect { name ->
 *     println("Name: $name")
 * }
 *
 * // Remove / Clear
 * prefs.remove("user_name")
 * prefs.clear()
 * ```
 */
class MiruPreferences(
    private val dataStore: DataStore<Preferences>,
) {

    // ──────────────────────────────────────────
    // String
    // ──────────────────────────────────────────

    fun observeString(key: String, default: String = ""): Flow<String> =
        dataStore.data.map { it[stringPreferencesKey(key)] ?: default }

    suspend fun getString(key: String, default: String = ""): String =
        observeString(key, default).first()

    suspend fun putString(key: String, value: String) {
        dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    // ──────────────────────────────────────────
    // Int
    // ──────────────────────────────────────────

    fun observeInt(key: String, default: Int = 0): Flow<Int> =
        dataStore.data.map { it[intPreferencesKey(key)] ?: default }

    suspend fun getInt(key: String, default: Int = 0): Int =
        observeInt(key, default).first()

    suspend fun putInt(key: String, value: Int) {
        dataStore.edit { it[intPreferencesKey(key)] = value }
    }

    // ──────────────────────────────────────────
    // Long
    // ──────────────────────────────────────────

    fun observeLong(key: String, default: Long = 0L): Flow<Long> =
        dataStore.data.map { it[longPreferencesKey(key)] ?: default }

    suspend fun getLong(key: String, default: Long = 0L): Long =
        observeLong(key, default).first()

    suspend fun putLong(key: String, value: Long) {
        dataStore.edit { it[longPreferencesKey(key)] = value }
    }

    // ──────────────────────────────────────────
    // Float
    // ──────────────────────────────────────────

    fun observeFloat(key: String, default: Float = 0f): Flow<Float> =
        dataStore.data.map { it[floatPreferencesKey(key)] ?: default }

    suspend fun getFloat(key: String, default: Float = 0f): Float =
        observeFloat(key, default).first()

    suspend fun putFloat(key: String, value: Float) {
        dataStore.edit { it[floatPreferencesKey(key)] = value }
    }

    // ──────────────────────────────────────────
    // Double
    // ──────────────────────────────────────────

    fun observeDouble(key: String, default: Double = 0.0): Flow<Double> =
        dataStore.data.map { it[doublePreferencesKey(key)] ?: default }

    suspend fun getDouble(key: String, default: Double = 0.0): Double =
        observeDouble(key, default).first()

    suspend fun putDouble(key: String, value: Double) {
        dataStore.edit { it[doublePreferencesKey(key)] = value }
    }

    // ──────────────────────────────────────────
    // Boolean
    // ──────────────────────────────────────────

    fun observeBoolean(key: String, default: Boolean = false): Flow<Boolean> =
        dataStore.data.map { it[booleanPreferencesKey(key)] ?: default }

    suspend fun getBoolean(key: String, default: Boolean = false): Boolean =
        observeBoolean(key, default).first()

    suspend fun putBoolean(key: String, value: Boolean) {
        dataStore.edit { it[booleanPreferencesKey(key)] = value }
    }

    // ──────────────────────────────────────────
    // Nullable String (useful for tokens, optional values)
    // ──────────────────────────────────────────

    fun observeStringOrNull(key: String): Flow<String?> =
        dataStore.data.map { it[stringPreferencesKey(key)] }

    suspend fun getStringOrNull(key: String): String? =
        observeStringOrNull(key).first()

    // ──────────────────────────────────────────
    // Remove / Clear
    // ──────────────────────────────────────────

    suspend fun remove(key: String) {
        dataStore.edit { it.remove(stringPreferencesKey(key)) }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
