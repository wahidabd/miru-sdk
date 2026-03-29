package com.miru.sdk.sample.data.source.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.miru.sdk.sample.data.model.BookmarkEntity

/**
 * Room database for the sample app.
 */
@Database(
    entities = [BookmarkEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(SampleDatabaseConstructor::class)
abstract class SampleDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}

/**
 * Room auto-generates this constructor.
 */
expect object SampleDatabaseConstructor : RoomDatabaseConstructor<SampleDatabase>
