pluginManagement {
    includeBuild("convention")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "miru-sdk"

include(":miru-sdk")
include(":core")
include(":network")
include(":ui-state")
include(":navigation")
include(":ui-components")
include(":di")
include(":firebase")
include(":auth")
include(":persistent")
include(":sample")
include(":sample:app")
