import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    explicitApi()
    jvmToolchain(17)

    wasmJs {
        moduleName = "diary"
        browser {
            commonWebpackConfig {
                outputFileName = "diary.js"
            }

            runTask {
                devServerProperty.set(devServerProperty.get().copy(port = 8080))
            }
        }

        binaries.executable()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        wasmJsMain {
            dependencies {
                implementation(project(":app:platform:common"))
                implementation(compose.material3)
            }
        }
    }
}
