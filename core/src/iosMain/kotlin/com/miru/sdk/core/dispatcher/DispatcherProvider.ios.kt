package com.miru.sdk.core.dispatcher

import kotlinx.coroutines.Dispatchers

/**
 * iOS implementation of DispatcherProvider.
 */
internal class IosDispatcherProvider : DispatcherProvider {
    override val main = Dispatchers.Main
    override val io = Dispatchers.Default
    override val default = Dispatchers.Default
    override val unconfined = Dispatchers.Unconfined
}

/**
 * Provides an iOS DispatcherProvider instance.
 */
actual fun provideDispatcher(): DispatcherProvider = IosDispatcherProvider()
