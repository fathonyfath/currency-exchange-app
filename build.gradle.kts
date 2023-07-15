plugins {
    id("com.android.application") version libs.versions.agp apply false
    id("com.android.library") version libs.versions.agp apply false
    kotlin("android") version libs.versions.kotlin apply false
    kotlin("multiplatform") version libs.versions.kotlin apply false
    kotlin("native.cocoapods") version libs.versions.kotlin apply false
    kotlin("plugin.serialization") version libs.versions.kotlin apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
