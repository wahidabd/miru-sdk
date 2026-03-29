package com.miru.sdk.sample

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Creates the root UIViewController for the iOS app.
 * Called from Swift via `MainViewControllerKt.MainViewController()`.
 */
fun MainViewController() = ComposeUIViewController { SampleApp() }
