package com.miru.sdk.persistent.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal const val PREFERENCES_FILE = "miru_preferences.preferences_pb"

/**
 * Create a platform-specific [DataStore]<[Preferences]> instance.
 *
 * - Android: stores in `context.filesDir/datastore/`
 * - iOS: stores in `NSDocumentDirectory`
 */
expect fun createDataStore(): DataStore<Preferences>
