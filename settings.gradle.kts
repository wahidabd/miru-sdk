pluginManagement {
    includeBuild("convention")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
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
