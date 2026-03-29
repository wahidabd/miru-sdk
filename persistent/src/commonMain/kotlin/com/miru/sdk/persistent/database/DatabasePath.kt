package com.miru.sdk.persistent.database

/**
 * Get the platform-specific database file path.
 *
 * - Android: requires [Context], use [databasePath] from androidMain
 * - iOS: uses NSDocumentDirectory, use [databasePath] from iosMain
 */
expect fun databasePath(name: String): String
