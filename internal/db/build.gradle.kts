plugins {
    aliasNoVersion(commonLibs.plugins.kotlin.multiplatform)
    aliasNoVersion(commonLibs.plugins.agp.library)
    aliasNoVersion(commonLibs.plugins.ksp)
    aliasNoVersion(commonLibs.plugins.sqlDelight)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
}

android {
    namespace = "dev.fathony.currencyexchange.internal.db"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }
}