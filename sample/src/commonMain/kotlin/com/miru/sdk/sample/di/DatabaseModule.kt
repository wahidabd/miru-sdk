package com.miru.sdk.sample.di

import com.miru.sdk.sample.data.source.local.SampleDatabase

/**
 * Expect function to create the platform-specific Room database instance.
 * Android and iOS have different Room.databaseBuilder calls.
 */
expect fun createSampleDatabase(): SampleDatabase
