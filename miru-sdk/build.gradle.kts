plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("publish")
}

kotlin {
    androidLibrary {
        namespace = "com.miru.sdk"
        compileSdk = 36
        minSdk = 24
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            api(project(":core"))
            api(project(":network"))
            api(project(":ui-state"))
            api(project(":navigation"))
            api(project(":ui-components"))
            api(project(":di"))
            api(project(":firebase"))
            api(project(":auth"))
            api(project(":persistent"))
        }
    }
}
