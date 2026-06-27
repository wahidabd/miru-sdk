plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("publish")
}

kotlin {
    android {
        namespace = "com.miru.sdk.di"
        compileSdk = 37
        minSdk = 24
    }

    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":network"))
            implementation(libs.ktor.client.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            compileOnly(libs.chucker)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

