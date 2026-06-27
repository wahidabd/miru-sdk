package com.miru.sdk.di.modules

import com.chuckerteam.chucker.api.ChuckerInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<List<Any>>(named("networkInterceptors")) {
        listOf(
            ChuckerInterceptor.Builder(androidContext()).build()
        )
    }
}
