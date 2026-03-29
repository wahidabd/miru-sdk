package com.miru.sdk.persistent.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * Miru Database — utility functions for building Room KMP databases.
 *
 * Provides pre-configured [RoomDatabase.Builder] with [BundledSQLiteDriver]
 * and coroutine dispatcher so consuming apps don't have to repeat boilerplate.
 *
 * Usage:
 * ```
 * // 1. Define your Database in commonMain
 * @Database(entities = [UserEntity::class], version = 1)
 * @ConstructedBy(AppDatabaseConstructor::class)
 * abstract class AppDatabase : RoomDatabase() {
 *     abstract fun userDao(): UserDao
 * }
 *
 * expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
 *
 * // 2. Build with the Miru helper
 * // Android:
 * val db = Room.databaseBuilder<AppDatabase>(context, dbPath)
 *     .miruBuild()
 *
 * // iOS:
 * val db = Room.databaseBuilder<AppDatabase>(dbPath)
 *     .miruBuild()
 * ```
 */
object MiruDatabase {

    /**
     * Build a [RoomDatabase] from the given builder, pre-configured with
     * [BundledSQLiteDriver] and [Dispatchers.IO].
     */
    fun <T : RoomDatabase> build(builder: RoomDatabase.Builder<T>): T {
        return builder
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}

/**
 * Extension function to build a [RoomDatabase] with Miru defaults.
 *
 * Pre-configures [BundledSQLiteDriver] and [Dispatchers.IO].
 */
fun <T : RoomDatabase> RoomDatabase.Builder<T>.miruBuild(): T {
    return MiruDatabase.build(this)
}
