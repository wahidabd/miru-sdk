plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
}

kotlin {
    androidLibrary {
        namespace = "com.miru.sdk.firebase"
        compileSdk = 35
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
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.napier)
            implementation(libs.koin.core)

            // Firebase KMP (GitLive)
            implementation(libs.firebase.common)
            implementation(libs.firebase.config)
            implementation(libs.firebase.messaging)
        }

        androidMain.dependencies {
            // Firebase Android SDK — explicit versions (no BOM, platform() deprecated in KMP)
            implementation(libs.firebase.android.common)
            implementation(libs.firebase.android.config)
            implementation(libs.firebase.android.messaging)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
