import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    explicitApi()
    jvmToolchain(17)

    jvm()

    wasmJs {
        browser()
    }

    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.material3)
                implementation(compose.components.resources)
            }
        }
    }
}

android {
    namespace = "io.github.taetae98coding.diary.feature.more"

    defaultConfig {
        compileSdk = 35
        minSdk = 33
    }

    buildFeatures {
        compose = true
    }
}
