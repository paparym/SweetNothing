// TODO: Check on removing it
buildscript {
    dependencies {
        classpath(libs.sqlDelight.gradle)
        classpath(libs.moko.resourcesGradle)
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.jvm) apply false
    alias(libs.plugins.compose) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
