plugins {
    alias(commonLibs.plugins.kotlin.multiplatform) apply false
    alias(commonLibs.plugins.kotlin.android) apply false
    alias(commonLibs.plugins.kotlin.plugin.serialization) apply false
    alias(commonLibs.plugins.agp.application) apply false
    alias(commonLibs.plugins.agp.library) apply false
    alias(commonLibs.plugins.ksp) apply false
    alias(commonLibs.plugins.nativeCoroutines) apply false
    alias(commonLibs.plugins.sqlDelight) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
