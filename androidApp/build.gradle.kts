@file:Suppress("UnstableApiUsage")

plugins {
    aliasNoVersion(commonLibs.plugins.agp.application)
    aliasNoVersion(commonLibs.plugins.kotlin.android)
}

android {
    namespace = "dev.fathony.currencyexchange.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "dev.fathony.currencyexchange.android"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = androidLibs.versions.androidx.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(platform(androidLibs.androidx.compose.bom))
    implementation(project(":shared"))
    implementation(androidLibs.bundles.androidx.compose.runtime)
    implementation(androidLibs.androidx.activity.compose)
}
