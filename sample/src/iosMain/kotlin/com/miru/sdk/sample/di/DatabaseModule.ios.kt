package com.miru.sdk.sample.di

import androidx.room.Room
import com.miru.sdk.persistent.database.databasePath
import com.miru.sdk.persistent.database.miruBuild
import com.miru.sdk.sample.data.source.local.SampleDatabase

actual fun createSampleDatabase(): SampleDatabase {
    return Room.databaseBuilder<SampleDatabase>(
        name = databasePath("miru_sample.db")
    ).miruBuild()
}
