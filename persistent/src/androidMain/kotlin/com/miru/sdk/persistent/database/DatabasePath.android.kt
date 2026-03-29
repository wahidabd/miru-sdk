package com.miru.sdk.persistent.database

import android.content.Context

private lateinit var appContext: Context

/**
 * Initialize the database path resolver with Android application context.
 * Call this once during app startup (e.g. in `Application.onCreate()`).
 *
 * Note: If you already called [MiruPreferencesInitializer.init], you still
 * need to call this separately for Room database path resolution.
 *
 * ```
 * MiruDatabaseInitializer.init(applicationContext)
 * ```
 */
object MiruDatabaseInitializer {
    fun init(context: Context) {
        appContext = context.applicationContext
    }
}

actual fun databasePath(name: String): String {
    return appContext.getDatabasePath(name).absolutePath
}
