plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(17)
    explicitApi()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries {
            framework {
                baseName = "Kotlin"
            }
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        iosMain {
            dependencies {
                implementation(project(":app:platform:common"))
                implementation(compose.ui)
            }
        }
    }
}