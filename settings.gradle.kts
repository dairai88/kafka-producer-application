// https://docs.gradle.org/current/userguide/toolchains.html
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

rootProject.name = "kafka-producer-application"
