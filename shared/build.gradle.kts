@file:Suppress("UNUSED_VARIABLE", "UnstableApiUsage")

plugins {
    aliasNoVersion(commonLibs.plugins.kotlin.multiplatform)
    aliasNoVersion(commonLibs.plugins.kotlin.plugin.serialization)
    aliasNoVersion(commonLibs.plugins.agp.library)
    aliasNoVersion(commonLibs.plugins.ksp)
    aliasNoVersion(commonLibs.plugins.nativeCoroutines)
    aliasNoVersion(commonLibs.plugins.sqlDelight)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
        val commonMain by getting {
            dependencies {
                implementation(commonLibs.ktor.client.core)
                implementation(commonLibs.ktor.client.contentNegotiation)
                implementation(commonLibs.ktor.serialization.kotlinx.json)
                implementation(commonLibs.kotlinx.coroutines.core)
                implementation(commonLibs.kotlinx.serialization.json)
                implementation(commonLibs.multiplatformSettings)
                implementation(commonLibs.sqlDelight.coroutinesExtension)
                implementation(commonLibs.kermit)
                implementation(project(":internal:db"))
                api(commonLibs.kotlinx.dateTime)
                api(commonLibs.bigNum)
                api(commonLibs.kotlinResult)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(commonLibs.kotlinx.coroutines.test)
                implementation(commonLibs.turbine)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(commonLibs.ktor.client.okhttp)
            }
        }
        val androidUnitTest by getting
        val androidInstrumentedTest by getting
        val iosMain by getting {
            dependencies {
                implementation(commonLibs.ktor.client.darwin)
            }
        }
        val iosTest by getting
    }
}

android {
    namespace = "dev.fathony.currencyexchange"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}
