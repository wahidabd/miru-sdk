import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.vanniktech.maven.publish")
}

val artifactId = if (project.name == "miru-sdk") "miru-sdk" else "miru-sdk-${project.name}"

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(project.group.toString(), artifactId, project.version.toString())

    pom {
        name.set(artifactId)
        description.set("Kotlin Multiplatform SDK for accelerating mobile development")
        inceptionYear.set("2025")
        url.set("https://github.com/wahidabd/miru-sdk")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("wahidabd")
                name.set("Wahid Dev")
                email.set("wahed.blog99@gmail.com")
                url.set("https://github.com/wahidabd")
            }
        }

        scm {
            url.set("https://github.com/wahidabd/miru-sdk")
            connection.set("scm:git:git://github.com/wahidabd/miru-sdk.git")
            developerConnection.set("scm:git:ssh://git@github.com/wahidabd/miru-sdk.git")
        }
    }
}
