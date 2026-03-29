import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

// Load API key from env.properties
val envProperties = Properties().apply {
    val envFile = rootProject.file("sample/env.properties")
    if (envFile.exists()) {
        envFile.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.miru.sdk.sample.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.miru.sdk.sample"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        // Inject NEWS_API_KEY into BuildConfig
        buildConfigField(
            "String",
            "NEWS_API_KEY",
            "\"${envProperties.getProperty("NEWS_API_KEY", "")}\""
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    // Core library desugaring (required by firebase-config GitLive)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // The shared KMP sample library — brings all composables & logic
    implementation(project(":sample"))

    // Activity Compose (needed for setContent in MainActivity)
    implementation(libs.androidx.activity.compose)

    // Koin Android (needed by SampleApplication)
    implementation(libs.koin.android)

    // SDK modules needed for initialization in SampleApplication
    implementation(project(":di"))
    implementation(project(":network"))
    implementation(project(":persistent"))

    // Logging
    implementation(libs.napier)
}
