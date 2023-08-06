@file:Suppress("UNUSED_VARIABLE")

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

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
        val commonMain by getting {
            dependencies {
                implementation(commonLibs.sqlDelight.coroutinesExtension)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(commonLibs.sqlDelight.androidDriver)
            }
        }
        val androidUnitTest by getting
        val androidInstrumentedTest by getting
        val iosMain by getting {
            dependencies {
                implementation(commonLibs.sqlDelight.nativeDriver)
            }
        }
        val iosTest by getting
    }
}

android {
    namespace = "dev.fathony.currencyexchange.internal.db"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("dev.fathony.currencyexchange.internal.db.sqldelight")
        }
    }
}
