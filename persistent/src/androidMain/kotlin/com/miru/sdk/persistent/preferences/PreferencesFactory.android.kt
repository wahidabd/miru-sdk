package com.miru.sdk.persistent.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

private lateinit var appContext: Context

/**
 * Initialize the DataStore with the Android application context.
 * Call this once during app startup (e.g. in `Application.onCreate()`).
 *
 * ```
 * MiruPreferencesInitializer.init(applicationContext)
 * ```
 */
object MiruPreferencesInitializer {
    fun init(context: Context) {
        appContext = context.applicationContext
    }
}

actual fun createDataStore(): DataStore<Preferences> {
    val path = appContext.filesDir.resolve("datastore/$PREFERENCES_FILE").absolutePath
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { path.toPath() },
    )
}
