plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("publish")
}

kotlin {
    androidLibrary {
        namespace = "com.miru.sdk.ui.components"
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

            // Compose — exposed as api() since public API uses Compose types
            @Suppress("DEPRECATION")
            api(compose.runtime)
            @Suppress("DEPRECATION")
            api(compose.foundation)
            @Suppress("DEPRECATION")
            api(compose.material3)
            @Suppress("DEPRECATION")
            api(compose.materialIconsExtended)
            @Suppress("DEPRECATION")
            api(compose.ui)
            @Suppress("DEPRECATION")
            api(compose.components.resources)

            // Image loading
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
