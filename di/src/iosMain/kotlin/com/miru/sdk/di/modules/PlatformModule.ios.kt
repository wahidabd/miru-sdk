package com.miru.sdk.di.modules

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<List<Any>>(named("networkInterceptors")) {
        emptyList()
    }
}
