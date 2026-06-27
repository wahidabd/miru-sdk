package com.miru.sdk.di.modules

import com.miru.sdk.core.logger.MiruLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<List<Any>>(named("networkInterceptors")) {
        try {
            val context = androidContext()
            val interceptor = com.chuckerteam.chucker.api.ChuckerInterceptor.Builder(context).build()
            listOf(interceptor)
        } catch (e: Throwable) {
            MiruLogger.w(
                tag = "PlatformModule",
                message = "Chucker interceptor unavailable: ${e.message}. " +
                    "Ensure you call MiruSdkInitializer.initialize(context, config) on Android " +
                    "and add both chucker:library (debug) and chucker:library-no-op (release) " +
                    "to your app dependencies."
            )
            emptyList()
        }
    }
}
