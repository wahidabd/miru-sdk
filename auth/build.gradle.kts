plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("publish")
}

kotlin {
    androidLibrary {
        namespace = "com.miru.sdk.auth"
        compileSdk = 36
        minSdk = 24
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.napier)
            implementation(libs.koin.core)

            // KMPAuth — standalone Google (no Firebase)
            implementation(libs.kmpauth.google)
            implementation(libs.kmpauth.uihelper)

            // Compose
            @Suppress("DEPRECATION")
            implementation(compose.runtime)
            @Suppress("DEPRECATION")
            implementation(compose.foundation)
            @Suppress("DEPRECATION")
            implementation(compose.material3)
        }

        androidMain.dependencies {
            // Facebook Android SDK
            implementation(libs.facebook.login)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
