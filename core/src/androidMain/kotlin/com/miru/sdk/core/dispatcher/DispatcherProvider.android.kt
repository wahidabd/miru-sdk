package com.miru.sdk.core.dispatcher

import kotlinx.coroutines.Dispatchers

/**
 * Android implementation of DispatcherProvider.
 */
internal class AndroidDispatcherProvider : DispatcherProvider {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
    override val default = Dispatchers.Default
    override val unconfined = Dispatchers.Unconfined
}

/**
 * Provides an Android DispatcherProvider instance.
 */
actual fun provideDispatcher(): DispatcherProvider = AndroidDispatcherProvider()
