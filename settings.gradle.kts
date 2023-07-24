@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("commonLibs") {
            from(files("gradle/commonLibs.versions.toml"))
        }
        create("androidLibs") {
            from(files("gradle/androidLibs.versions.toml"))
        }
    }
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CurrencyExchange"
include(":androidApp")
include(":shared")