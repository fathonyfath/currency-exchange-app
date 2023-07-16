plugins {
    alias(commonLibs.plugins.gradleVersion)
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
