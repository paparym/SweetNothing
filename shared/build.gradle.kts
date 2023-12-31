// TODO: disable for gradle 8.1 +
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinX.serialization.plugin)
    alias(libs.plugins.compose)
    id(libs.plugins.mokoResources.get().pluginId)
    id(libs.plugins.sqlDelight.plugin.get().pluginId)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
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
            isStatic = true
//            export("dev.icerock.moko:resources:0.23.0")
//            export("dev.icerock.moko:graphics:0.9.0") // toUIColor here
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.sqlDelight.runtime)
                implementation(libs.sqlDelight.coroutine)
                implementation(libs.kotlinX.dateTime)

                implementation(libs.ktor.core)
                implementation(libs.ktor.contentNegotiation)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.json)

                implementation(libs.kotlinX.serializationJson)

                implementation(libs.moko.mvvm)
                implementation(libs.moko.mvvmCompose)
                implementation(libs.moko.mvvmFlow)
                implementation(libs.moko.mvvmFlowCompose)

                implementation(libs.kamel)

                implementation(libs.voyager.core)
                implementation(libs.voyager.tabNavigator)
                implementation(libs.voyager.navigator)

                implementation(libs.koin.core)

                api(libs.moko.resources)
                api(libs.moko.resourcesCompose)

                implementation(libs.decompose.core)
                implementation(libs.decompose.extensions)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.moko.resourcesTest)
            }
        }
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.sqlDelight.android)
                implementation(libs.appCompat)
                implementation(libs.compose.activity)
                implementation(libs.ktor.android)
                implementation(libs.koin.android)
                implementation(libs.decompose.core)
            }
        }
        val iosMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.sqlDelight.native)
                implementation(libs.ktor.ios)
            }
        }
    }
}

sqldelight {
    database("GameDatabase") {
        packageName = "com.prestigerito.sweetnothing.database"
        sourceFolders = listOf("sqldelight")
    }
}

android {
    namespace = "com.prestigerito.sweetnothing"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.prestigerito.sweetnothing" // required
}
