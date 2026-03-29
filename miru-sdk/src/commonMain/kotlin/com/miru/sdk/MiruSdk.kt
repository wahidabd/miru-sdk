package com.miru.sdk

/**
 * Miru SDK umbrella module.
 *
 * This module re-exports all Miru SDK sub-modules as transitive `api()` dependencies,
 * so consumers only need a single dependency declaration:
 *
 * ```kotlin
 * implementation("com.github.wahidabd.miru-sdk:miru-sdk:<version>")
 * ```
 */
public object MiruSdk {
    public const val VERSION: String = "0.1.3"
}
