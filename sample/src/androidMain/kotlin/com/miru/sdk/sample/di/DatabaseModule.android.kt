package com.miru.sdk.sample.di

import android.content.Context
import androidx.room.Room
import com.miru.sdk.persistent.database.miruBuild
import com.miru.sdk.sample.data.source.local.SampleDatabase

private lateinit var appContext: Context

/**
 * Initialize the sample database context.
 * Call this once during app startup (e.g. in `Application.onCreate()`).
 */
object SampleDatabaseInitializer {
    fun init(context: Context) {
        appContext = context.applicationContext
    }
}

actual fun createSampleDatabase(): SampleDatabase {
    return Room.databaseBuilder<SampleDatabase>(
        context = appContext,
        name = appContext.getDatabasePath("miru_sample.db").absolutePath
    ).miruBuild()
}
