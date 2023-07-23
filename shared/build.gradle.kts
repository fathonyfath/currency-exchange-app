@file:Suppress("UNUSED_VARIABLE", "UnstableApiUsage")

plugins {
    alias(commonLibs.plugins.kotlin.multiplatform)
    alias(commonLibs.plugins.kotlin.plugin.serialization)
    alias(commonLibs.plugins.agp.library)
    alias(commonLibs.plugins.ksp)
    alias(commonLibs.plugins.nativeCoroutines)
    alias(commonLibs.plugins.sqlDelight)
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
                api(commonLibs.kotlinx.dateTime)
                api(commonLibs.bigNum)
                api(commonLibs.kotlinResult)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(commonLibs.ktor.client.okhttp)
                implementation(commonLibs.sqlDelight.androidDriver)
            }
        }
        val androidUnitTest by getting
        val androidInstrumentedTest by getting
        val iosMain by getting {
            dependencies {
                implementation(commonLibs.ktor.client.darwin)
                implementation(commonLibs.sqlDelight.nativeDriver)
            }
        }
        val iosTest by getting
    }
}

android {
    namespace = "dev.fathony.currencyexchange"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("dev.fathony.currencyexchange.sqldelight")
        }
    }
}
